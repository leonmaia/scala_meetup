package com.meetup.server

import com.meetup.filter.AccessLoggingFilter
import com.meetup.persistence.Medias
import com.meetup.service.{EvalService, HealthCheckService, MediaService}
import com.twitter.finagle.http.path.{->, /, Long, Root}
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Method, Request}

class Router {

  lazy private val medias = new Medias()

  lazy private val mediaService: MediaService = MediaService(medias)

  private val routingService = RoutingService.byMethodAndPathObject[Request] {
    case Method.Post -> Root / "eval" => new EvalService()
    case Method.Get -> Root / "status" => new HealthCheckService()
    case Method.Get -> Root / "media" / Long(id) => mediaService.get(id)
    case Method.Get -> Root / "media" => mediaService.all
    case Method.Post -> Root / "media" => mediaService.add
  }

  private val filters = new AccessLoggingFilter()

  def create = filters andThen routingService
}