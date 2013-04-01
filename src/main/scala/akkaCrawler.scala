package scalacrawler

import akka.actor._
import com.ning.http.client._
import java.net.URL

object a extends AkkaCrawler {
  def as = ActorSystem("fu")
  def client = new AsyncHttpClient
}

trait AkkaCrawler extends AsyncHttpClientCrawler {

  def client: AsyncHttpClient
  def as: ActorSystem

  def processUrls(urls: Iterable[URL], callbackActor: ActorRef) = {
    urls foreach { url =>
      invoke(get(url), new AkkaCrawlerCallback(callbackActor, url))
    }
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



object SprayCrawlingHandlerActorProtocol {
  case class CrawlingRequest(reqId: Long, urls: Set[URL])
  case class ResponseReceived(reqId: Long, url: URL, response: Response)
}

class SprayCrawlingHandlerActor(val next: ActorRef, var urls: Set[URL] )
    extends Actor {


  def receive = {
    case msg @ (url: URL, _) => {
      urls = urls - url
      next ! msg
      if(urls.isEmpty) {

      }
    }
  }
}
