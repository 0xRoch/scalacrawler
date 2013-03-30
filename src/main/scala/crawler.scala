package crawler

import scala.language.implicitConversions

import akka.actor._
import scala.concurrent.Future
import java.util.concurrent.{ Future => JFuture }
import com.ning.http.client._
import java.net.URL

trait Crawler[CrawlResponse] {

  import HttpReader._

  type CrawlResult = (URL, CrawlResponse)

  implicit def actorSystem: ActorSystem

  def httpClient: AsyncHttpClient

  def fetchUrls(urls: Seq[URL]): Future[Seq[Http[CrawlResult]]]
  def fetchUrl(url: URL): JFuture[Http[CrawlResult]]
  def collectFuture[A](future: JFuture[A], collector: ActorRef)

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
