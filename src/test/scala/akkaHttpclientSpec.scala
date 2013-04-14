package scalacrawler

import org.scalatest._
import mock._
import akka.testkit._
import akka.actor._

class AkkaHandlerSpecs extends
    TestKit(ActorSystem("system")) with
    WordSpec with
    StopSystemAfterAll with
    MockitoSugar {

  object akkaCli extends AkkaAsyncHttpClientModule with MockClient

  val fixture = new {
    val handler = new akkaCli.AkkaHandler(testActor)
  }

  "onComplete" should {
    "bang reponse to actor properly" in {
      val response = mock[akkaCli.HttpResponse]
      fixture.handler.onComplete(response)
      expectMsg(response)
    }
  }

  "onFailure" should {
    "bang failure to actor properly" in {
      val failure = mock[akkaCli.Failure]
      fixture.handler.onFailure(failure)
      expectMsg(failure)
    }
  }

}

class CrawlingHandlerForRequestActorSpec extends
    TestKit(ActorSystem("system")) with
    WordSpec with
    StopSystemAfterAll with
    MockitoSugar {

  object akkaCli extends AkkaAsyncHttpClientModule with MockClient

  "actor" should {
    "send a proper json when receiving a proper crawling response" in {
      val uri = new java.net.URI("http://localhost/")
      val statusCode = 1337
      val body = "<html> sup son </html>"
      val inputStream = mock[java.io.InputStream]
      val response = new akkaCli.MockResponse(uri, statusCode, body, inputStream)

      val testee = actorOf(new CrawlingHandlerForRequestActor())

    }

    "send a proper json after receiving a failure" in {

    }

  }



}
