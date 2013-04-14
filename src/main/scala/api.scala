package scalacrawler

import java.net.URI
import RequestId.RequestId
import spray.json._

object API {
  case class Metadata(
    callback: URI,
    requestid: RequestId,
    client_meta: JsValue = JsNull)
}
