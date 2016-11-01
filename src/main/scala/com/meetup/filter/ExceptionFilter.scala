package com.meetup.filter

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import com.meetup.http.Responses._
import com.rigon.zipkin.traced
import com.twitter.util.Duration._
import org.slf4j.LoggerFactory

class ExceptionFilter extends SimpleFilter[Request, Response] {

  lazy val log = LoggerFactory.getLogger(getClass.getName)

  @traced("exception-filter", "filter", fromSeconds(1))
  def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    service(request) handle {
      case e: IllegalStateException => respond(e.getMessage, status = Status.BadRequest)
      case e: IllegalArgumentException => respond(e.getMessage, status = Status.BadRequest)
      case t: Throwable =>
        log.error(
          s"""
             |class: ${t.getClass.getName}
             |message: ${t.getMessage}
             |cause: ${t.getCause}
             |stackTrace: ${t.getStackTrace}
           """.stripMargin)
        respond(None, Status.InternalServerError)
    }
  }
}

