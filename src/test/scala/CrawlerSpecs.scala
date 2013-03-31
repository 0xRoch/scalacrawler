package scalacrawler

import org.scalatest._
import mock.MockitoSugar
import org.mockito.Mockito._
import com.ning.http.client._
import java.net.URL

class ExampleSpec extends FlatSpec with MockitoSugar with ShouldMatchers {
  object Crawler extends Crawler

  "Crawler" should "build simple http get requests which follow redirects" in {
    val url = "http://someurl.com"
    val req = Crawler.simpleGet(new URL(url))
    req.getMethod should equal ("GET")
    req.getRawUrl should equal (url)
    req.isRedirectEnabled should equal (true)
  }

  it should "should call execute on proper request with proper handler in invoe method" in {
    val http = mock[AsyncHttpClient]
    val request = mock[Request]
    val handler = mock[AsyncHandler[Response]]

    Crawler.invoke(request, handler)(http)
    verify(http).executeRequest(request, handler)
  }
}
