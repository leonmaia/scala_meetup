package com.meetup.service

import com.meetup.http.Responses._
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future

class HealthCheckService extends Service[Request, Response] {
  override def apply(request: Request): Future[Response] = Future(respond("WORKING"))
}
