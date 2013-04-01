package scalacrawler

import org.scalatest._
import mock.MockitoSugar
import org.mockito.Mockito._
import com.ning.http.client._
import java.net.URL

class AsyncHttpClientCrawlerSpec extends WordSpec with MockitoSugar with ShouldMatchers {

  val crawler = new AsyncHttpClientCrawler{
    val client = mock[AsyncHttpClient]
  }

  "AsyncHttpClientCrawler" should {
    "build simple http get requests which follow redirects" in {
      val url = "http://someurl.com"
      val req = crawler.get(new URL(url))
      req.getMethod should equal ("GET")
      req.getRawUrl should equal (url)
      req.isRedirectEnabled should equal (true)
    }

    "should call execute on proper request with proper handler in invoke method" in {
      val request = mock[Request]
      val handler = mock[AsyncCompletionHandler[Response]]

      crawler.invoke(request, handler)
      verify(crawler.client).executeRequest(request, handler)
    }
  }
}
