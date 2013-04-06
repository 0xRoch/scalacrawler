package scalacrawler

import org.scalatest._
import mock._
import org.mockito.Mockito._
import com.ning.http.client.{AsyncHttpClient => NingHttpClient, _}
import java.net.URI

class NingAsyncHttpClientSpec extends WordSpec
    with MockitoSugar with ShouldMatchers {

  object RealNing extends NingAsyncHttpClient {
    val httpClient = new NingHttpClient()
  }
  object MockedNing extends NingAsyncHttpClient {
    val httpClient = mock[NingHttpClient]
  }

  val uri = new URI("http://example.org")


  "defaultGet" should {
    "return request with proper uri" in {
      RealNing.defaultGet(uri).uri should equal (uri)
      RealNing.defaultGet(uri).ningRequest.getUrl should equal (uri.toString)
    }
  }

  "execute" should {
    "call inner execute with proper arguments" in {
//      val req = MockedNing.defaultGet(uri)
    }
  }

}

class NingResponseHandlerSpecs extends WordSpec with MockitoSugar with ShouldMatchers{

  object Ning extends NingAsyncHttpClient {
    val httpClient = mock[NingHttpClient]

    type ResponseHandler = Handler
    class Handler extends ResponseHandlerApi {

      var resp: HttpResponse = null
      var fail: Failure = null

      def onComplete(r: HttpResponse) { resp = r }
      def onFailure(f: Failure) { fail = f }
    }
  }

  val uri = new URI("http://example.org")
  def fixture = new {
    val realH = new Ning.Handler
    val h = new Ning.NingResponseHandler(realH, uri)
  }

  "onComplete" should {
    "call outer handler with ning response" in {
      val mockResp = mock[Response]
      val f = fixture
      f.h.onCompleted(mockResp)
      f.realH.resp.response should equal(mockResp)
      f.realH.fail should equal(null)
    }
  }

  "onFailure" should {
    "call outer handler with proper failure" in {
      val failure = mock[Throwable]
      val f = fixture
      f.h.onThrowable(failure)
      f.realH.resp should equal(null)
      f.realH.fail should equal(Ning.Failure(uri, failure))
    }
  }

}
