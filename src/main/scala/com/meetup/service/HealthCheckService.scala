package com.meetup.service

import com.meetup.http.Responses._
import com.meetup.server.Main
import com.rigon.zipkin.traced
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Duration._
import com.twitter.util.Future

class HealthCheckService extends Service[Request, Response] {

  @traced("status-service", "http", fromSeconds(1))
  override def apply(request: Request): Future[Response] = {
    Future(respond(
      Map(
        "Service" -> Main.serviceName,
        "Port" -> Main.port,
        "Status" -> "WORKING"))
    )
  }
}
