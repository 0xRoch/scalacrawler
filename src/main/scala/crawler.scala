package scalacrawler

import scala.language.implicitConversions

import akka.actor._
import scala.concurrent.Future
import java.util.concurrent.{ Future => JFuture }
import com.ning.http.client._
import java.net.URL

trait Crawler {

  import HttpReader._

  def fetchUrls(buildHandler: URL => AsyncHandler[Response])(urls: Seq[URL]): Http[Unit] = {
    Http( cli => {
      urls foreach { x =>
        invoke(simpleGet(x), buildHandler(x))
      }
    })
  }

  def invoke(request: Request, callback: AsyncHandler[Response]): Http[Unit] =
    Http( _.executeRequest(request, callback) )

  def simpleGet(url: URL): Request =
    new RequestBuilder().setUrl(url.toString).setFollowRedirects(true).build

}


object HttpReader extends ReaderI[AsyncHttpClient] {
  type Http[A] = SpecificReader[A]
  def Http[A]: (AsyncHttpClient => A)=> Http[A] = reader _
}

trait ReaderI[R]  {
  type SpecificReader[A] = Reader[R, A]

  implicit def reader[A](f: R => A): SpecificReader[A] =
    Reader(f)

  def pure[A](a: A): SpecificReader[A] =
    (r:R) => a
}

case class Reader[R, A](f: R => A) {
  def apply(r: R) = f(r)

  def map[B](g: A => B): Reader[R, B] =
    Reader(r => g(f(r)) )

  def flatMap[B](g: A => Reader[R, B]): Reader[R, B] =
    Reader(r => g(f(r))(r))
}
