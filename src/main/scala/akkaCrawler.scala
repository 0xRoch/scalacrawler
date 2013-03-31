package scalacrawler

import akka.actor._
import com.ning.http.client._
import java.net.URL

trait AkkaCrawler { this: Crawler =>

  def httpClient: AsyncHttpClient
  def as: ActorSystem

  def processUrls(urls: Iterable[URL], callbackActor: ActorRef) = {
    urls foreach { url =>
      invoke(simpleGet(url), new AkkaCrawlerCallback(callbackActor, url))
    }
  }


  class AkkaCrawlerCallback(val callback: ActorRef, val url: URL)
      extends AsyncCompletionHandler[Response] {

    override def onCompleted(response: Response) = {
      callback ! (url, response)
      response
    }

    override def onThrowable(t: Throwable) {
      callback ! (url, t)
    }
  }


}

class SprayCrawlingHandlerActor(val responder: ActorRef, var urls: Set[URL] )
    extends Actor  {

  def receive = {
    case (url: URL, t: Throwable) => ???
    case (url: URL, resp: Response) => ???
  }
}
