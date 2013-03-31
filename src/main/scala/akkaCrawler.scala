package scalacrawler

import akka.actor._
import com.ning.http.client._
import java.net.URL

trait AkkaCrawler extends Crawler {

  def httpClient: AsyncHttpClient
  def as: ActorSystem

  def processUrlList(urls: List[URL], callbackActor: ActorRef)  {
    fetchUrls((x: URL) => new AkkaCallback(callbackActor, x))(urls)
  }


  class AkkaCallback(val callback: ActorRef, val url: URL) extends AsyncCompletionHandler[Response] {
    override def onCompleted(response: Response) = {
      callback ! response
      response
    }

    override def onThrowable(t: Throwable) {
      callback ! t
    }
  }



  object SprayCrawlingHandlerActorProtocol {
    case class Process(urls: List[String], responder: ActorRef)
  }

  class SprayCrawlingHandlerActor extends Actor {
    import SprayCrawlingHandlerActorProtocol._
    var state: (Set[URL], ActorRef) = _

    def awaitDataToProcess: Receive = {
      case Process(urlStrings, responder) => {
        val urls = urlStrings.map(new URL(_)).toSet
        state = (urls, responder)
      }
    }

    def awaitCrawlingUpdates: Receive = {
      case x => ???
    }

    def receive = awaitDataToProcess

  }



}
