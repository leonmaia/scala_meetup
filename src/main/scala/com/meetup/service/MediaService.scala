package com.meetup.service

import java.util.UUID

import com.meetup.Media
import com.meetup.filter.CacheFilter
import com.meetup.http.Responses._
import com.meetup.http.readers._
import com.meetup.persistence.Medias
import com.rigon.zipkin.traced
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Duration._
import com.twitter.util.Future

case class MediaService(medias: Medias) {

  val add = new Service[Request, Response] {
    @traced("media-service#add", "http", fromSeconds(1))
    override def apply(request: Request): Future[Response] = medias.add(body[Media](request)) map (respond(_))
  }

  def get(id: UUID) = CacheFilter("public,max-age=60") andThen new Service[Request, Response] {
    @traced("media-service#get", "http", fromMilliseconds(100))
    override def apply(request: Request): Future[Response] = {
      medias.get(id) map {
        case Some(result: Array[Byte]) => respond(result)
        case None => throw new NoSuchElementException("")
      }
    }
  }
}
