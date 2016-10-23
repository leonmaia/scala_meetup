package com.meetup

import java.net.InetSocketAddress

import com.twitter.finagle.RequestTimeoutException
import com.twitter.finagle.tracing.Trace
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.Duration._
import com.twitter.util.{Duration, Future}


package object stats {
  def withTrace[T](id: String, timeout: Duration = fromSeconds(1), hostOpt: Option[InetSocketAddress] = Option.empty)
                  (block: => Future[T]) = {
    Trace.traceService(id, "custom", hostOpt) {
      withTimeout(id, timeout, block) map { res =>
        res
      }
    }
  }

  private def withTimeout[T](id: String = s"${getClass.getSimpleName}", duration: Duration, block: => Future[T]): Future[T] = {
    block.within(
      DefaultTimer.twitter,
      duration, {
        Trace.record(s"$id.timeout")
        new RequestTimeoutException(duration, s"Timeout exceed while accessing using $id")
      }
    )
  }
}
