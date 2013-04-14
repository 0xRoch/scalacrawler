package scalacrawler

import java.util.concurrent.atomic.AtomicLong

object RequestId {
  type RequestId = Long

  private val last = new AtomicLong(0)

  def next = {
    last.incrementAndGet()
  }

}
