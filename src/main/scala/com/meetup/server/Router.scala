package com.meetup.server

import com.meetup.filter.TraceFilter
import com.meetup.http.Responses.respond
import com.twitter.finagle.Service
import com.twitter.finagle.http.path.{->, /, Root}
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.util.Future

class Router {

  private val routingService = RoutingService.byMethodAndPathObject[Request] {
    case Method.Get -> Root / "healthcheck" => new Service[Request, Response] {
      override def apply(request: Request): Future[Response] = {
        Future(respond("WORKING"))
      }
    }
  }

  private val filters = TraceFilter()

  def create = filters andThen routingService
}
