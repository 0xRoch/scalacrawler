package crawler

import scala.language.implicitConversions

import akka.actor._
import scala.concurrent.Future
import java.util.concurrent.{ Future => JFuture }
import com.ning.http.client._
import java.net.URL

trait Crawler[CrawlResponse] extends HttpReader {

  implicit def actorSystem: ActorSystem

  val a: Reader[AsyncHttpClient, Int] = pure(3)


  //  def httpClient: AsyncHttpClient

  //  def fetchUrls(urls: Seq[URL]): Future[Seq[CrawlResult]]
  //  def fetchUrl(url: URL): JFuture[CrawlResult]
  // def collectFuture[A](future: JFuture[A], collector: Actor Ref)

}


trait HttpReader extends ReaderI[AsyncHttpClient] {
  def Http = reader _
  type Http[A] = SpecificReader[A]
}

trait ReaderI[R]  {
  type SpecificReader[A] = Reader[R, A]
  implicit def reader[R, A](f: R => A) = Reader(f)

  def pure[R,A](a: A): Reader[R, A] = (r: R) => a
}

case class Reader[R, A](f: R => A) {
  def apply(r: R) = f(r)

  def map[B](g: A => B): Reader[R, B] =
    Reader(r => g(f(r)) )

  def flatMap[B](g: A => Reader[R, B]): Reader[R, B] =
    Reader(r => g(f(r))(r))
}
