package com.meetup.filter

import com.meetup.stats._
import com.twitter.finagle.http._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Duration._
import com.twitter.util.{Duration, Future}
import org.slf4j.LoggerFactory

object TraceFilter {
  def apply(id: String): SimpleFilter[Request, Response] = {
    id match {
      case i if i.nonEmpty => new TraceFilter().trace(Option(i))
      case _ => new TraceFilter().trace()
    }
  }

  def apply(): SimpleFilter[Request, Response] = new TraceFilter().trace()
}

case class TraceFilter(timeout: Duration = fromSeconds(1)) {
  lazy val log = LoggerFactory.getLogger(getClass.getName)

  /**
    * A simple Finagle filter that send traces to Zipkin.
    */
  def trace(id: Option[String] = None): SimpleFilter[Request, Response] = new SimpleFilter[Request, Response] {
    def apply(req: Request, service: Service[Request, Response]): Future[Response] = {
      withTrace(id.getOrElse(req.uri), timeout) {
        service(req) map { res =>
          res
        }
      }
    }
  }
}