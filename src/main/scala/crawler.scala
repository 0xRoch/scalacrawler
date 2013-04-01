package scalacrawler

import scala.language.implicitConversions

import akka.actor._
import scala.concurrent.Future
import java.util.concurrent.{ Future => JFuture }
import com.ning.http.client._
import java.net.URL

trait AsyncCrawler {

  type HttpRequest
  type Callback


  def invoke(request: HttpRequest, callback: Callback)
  def get(url: URL): HttpRequest

}

trait  AsyncHttpClientCrawler extends AsyncCrawler {
  type HttpRequest = Request
  type Callback = AsyncHandler[Response]

  def client: AsyncHttpClient

  def invoke(request: Request, callback: Callback) {
    client.executeRequest(request, callback)
  }

  def get(url: URL): HttpRequest =
    new RequestBuilder().setUrl(url.toString).setFollowRedirects(true).build

}
