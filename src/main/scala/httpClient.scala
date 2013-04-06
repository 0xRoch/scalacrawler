package scalacrawler

import java.net.URI
import java.io.InputStream
import com.ning.http.client.{AsyncHttpClient => NingHttpClient, _}

trait AsyncHttpClient {

  type HttpRequest <: HttpRequestApi
  trait HttpRequestApi {
    def uri: URI
  }

  type HttpResponse <: HttpResponseApi
  trait HttpResponseApi {
    def uri: URI
    def statusCode: Int
    def body: String
    def bodyInputStream: InputStream
  }

  case class Failure(uri: URI, reason: Throwable)

  type ResponseHandler <: ResponseHandlerApi
  trait ResponseHandlerApi {
    def onComplete(response: HttpResponse): Unit
    def onFailure(f: Failure): Unit
  }

  def execute(request: HttpRequest, handler: ResponseHandler)

  def defaultGet(url: URI): HttpRequest
}


trait NingAsyncHttpClient extends AsyncHttpClient {

  def httpClient: NingHttpClient

  def defaultGet(uri: URI): HttpRequest  = {
    NingRequest(httpClient.prepareGet(uri.toString).build(), uri)
  }

  def execute(request: HttpRequest, handler: ResponseHandler) = {
    httpClient.executeRequest(request.ningRequest,
      new NingResponseHandler(handler, request.uri))
  }

  // plumbing
  type HttpRequest = NingRequest
  case class NingRequest(ningRequest: Request, uri: URI) extends HttpRequestApi

  class NingResponseHandler(
    val handler: ResponseHandler,
    val uri: URI
  ) extends AsyncCompletionHandler[Response]{
    override def onCompleted(response: Response) = {
      handler.onComplete(new NingHttpResponse(response))
      response
    }
    override def onThrowable(t: Throwable) { handler.onFailure(Failure(uri, t)) }
  }

  type HttpResponse = NingHttpResponse
  class NingHttpResponse(val response: Response) extends HttpResponseApi {
    def uri = response.getUri
    def statusCode = response.getStatusCode
    def body = response.getResponseBody
    def bodyInputStream = response.getResponseBodyAsStream
  }

}
