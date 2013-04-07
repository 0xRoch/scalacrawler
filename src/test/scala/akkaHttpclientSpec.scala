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

  object akkaCli extends AkkaAsyncHttpClient with MockClient

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
