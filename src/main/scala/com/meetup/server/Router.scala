package com.meetup.server

import com.meetup.filter.{AccessLoggingFilter, TraceFilter}
import com.meetup.service.HealthCheckService
import com.twitter.finagle.http.path.{->, /, Root}
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Method, Request}

class Router {

  private val routingService = RoutingService.byMethodAndPathObject[Request] {
    case Method.Post -> Root / "eval" => TraceFilter("eval-service") andThen new EvalService()
    case Method.Get -> Root / "status" => TraceFilter("healthCheck-service") andThen new HealthCheckService()
  }

  private val filters = new AccessLoggingFilter()

  def create = filters andThen routingService
}