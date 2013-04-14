package scalacrawler

import akka.actor._
import akka.testkit.TestKit

trait AkkaAsyncHttpClientModule extends AsyncHttpClient {

  type ResponseHandler = AkkaHandler
  class AkkaHandler(val handler: ActorRef) extends ResponseHandlerApi {
    def onComplete(resp: HttpResponse) { handler ! resp }
    def onFailure(f: Failure) { handler ! f }
  }



}
