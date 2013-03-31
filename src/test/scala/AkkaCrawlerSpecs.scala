package scalacrawler

import org.scalatest._
import mock.MockitoSugar
import akka.testkit.TestKit
import akka.actor._
import java.net.URL
import com.ning.http.client._

class AkkaCrawlerSpec extends TestKit(ActorSystem("testsystem"))
    with WordSpec
    with StopSystemAfterAll
    with ShouldMatchers
    with  MockitoSugar {

  trait AkkaCrawlerConfig {
    val as = system
    def httpClient = ???
  }

  trait MockCrawler extends Crawler {
    import HttpReader._
    var requests = Vector[(Request, AkkaCrawler.AkkaCrawlerCallback)]()

    override def invoke(r: Request, h: AsyncHandler[Response]) = {
      requests = requests :+ ((r,h.asInstanceOf[AkkaCrawler.AkkaCrawlerCallback]))
      Http(x => {})
    }
  }

  object AkkaCrawler extends AkkaCrawler with MockCrawler with AkkaCrawlerConfig
  def mockUrl = new URL("http://localhost/")
  def mockUrls: Set[URL] = Set(mockUrl, new URL("http://somethingelse.com/"))
  def callback = new AkkaCrawler.AkkaCrawlerCallback(testActor, mockUrl)

  "processUrls" should {
    "invoke proper requests" in {
      AkkaCrawler.processUrls(mockUrls, testActor)
      AkkaCrawler.requests.length should equal (2)
      val handlers = AkkaCrawler.requests.map(_._2).toSet
      val callbacks = handlers.map(_.callback)
      callbacks.size should equal (1)
      callbacks.head should equal (testActor)
      handlers.map(_.url) should equal(mockUrls)
      val a = AkkaCrawler.requests.map(_._1.getURI.toURL).toSet:Set[URL]
      a should equal (mockUrls)
    }
  }

  "AkkaCrawlerCallback" should {
    "send proper response to proper actor" in {
      val response = mock[Response]
      callback.onCompleted(response)
      expectMsg((mockUrl, response))
    }

    "send proper throwable to proper actor" in {
      val t = mock[Throwable]
      callback.onThrowable(t)
      expectMsg((mockUrl, t))
    }
  }


}
