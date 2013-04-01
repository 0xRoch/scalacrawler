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
    with MockitoSugar {

  trait AkkaCrawlerConfig {
    val as = system
    def client = ???
  }

  trait MockCrawler extends AsyncHttpClientCrawler {
    var requests = Vector[(HttpRequest, Callback)]()

    override def invoke(r: HttpRequest, h: Callback) {
      requests = requests :+ ((r,h))
    }

  }

  val akkaCrawler = new AkkaCrawler with MockCrawler with AkkaCrawlerConfig
  def mockUrl = new URL("http://localhost")
  def mockUrls: Set[URL] = Set(mockUrl, new URL("http://somethingelse.com"))
  def callback = new AkkaCrawlerCallback(testActor, mockUrl)

  "processUrls" should {
    "invoke proper requests" in {
      akkaCrawler.processUrls(mockUrls, testActor)
      akkaCrawler.requests.length should equal (2)
      val handlers = akkaCrawler.requests.map(_._2).map(_.asInstanceOf[AkkaCrawlerCallback]).toSet
      val callbacks = handlers.map(_.callback)
      callbacks.size should equal (1)
      callbacks.head should equal (testActor)
      val urls = handlers.map(_.url)
      urls should equal(mockUrls)
      akkaCrawler.requests.map(_._1.getUrl).toSet should equal (mockUrls.map(_.toString))
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
