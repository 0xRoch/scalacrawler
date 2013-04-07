package scalacrawler

import akka.testkit.TestKit
import org.scalatest.{Suite, BeforeAndAfterAll}
import java.net.URI
import java.io.InputStream

trait StopSystemAfterAll extends BeforeAndAfterAll {

  this: TestKit with Suite =>

  override protected def afterAll {
    super.afterAll()
    system.shutdown()
  }
}


trait MockClient extends AsyncHttpClient {
  type HttpResponse = MockResponse

  case class MockResponse(
    uri: URI,
    statusCode: Int,
    body: String,
    bodyInputStream: InputStream) extends HttpResponseApi

  def execute(request: HttpRequest, handler: ResponseHandler) = ???
  def defaultGet(uri: URI): HttpRequest = ???
}
