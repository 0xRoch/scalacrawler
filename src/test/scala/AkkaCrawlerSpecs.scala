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
    def httpClient = ???
  }

  trait MockCrawler extends AsyncCrawler {
    var requests = Vector[(MockRequest, Callback)]()

    type HttpRequest = MockRequest

    case class MockRequest(url: URL)

    def invoke(r: HttpRequest, h: Callback) {
      requests = requests :+ ((r,h))
    }

    def get(url: URL) = MockRequest(url)

  }

  val akkaCrawler = new AkkaCrawler with MockCrawler with AkkaCrawlerConfig
  def mockUrl = new URL("http://localhost/")
  def mockUrls: Set[URL] = Set(mockUrl, new URL("http://somethingelse.com/"))
  def callback = new akkaCrawler.AkkaCrawlerCallback(testActor, mockUrl)

  "processUrls" should {
    "invoke proper requests" in {
      akkaCrawler.processUrls(mockUrls, testActor)
      akkaCrawler.requests.length should equal (2)
      val handlers = akkaCrawler.requests.map(_._2).toSet
      val callbacks = handlers.map(_.callback)
      callbacks.size should equal (1)
      callbacks.head should equal (testActor)
      handlers.map(_.url) should equal(mockUrls)
      akkaCrawler.requests.map(_._1.url).toSet should equal (mockUrls)
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
