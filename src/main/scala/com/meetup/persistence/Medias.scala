package com.meetup.persistence

import java.util.UUID

import com.meetup.Media
import com.meetup.json.toBytes
import com.rigon.zipkin.traced
import com.twitter.cache.guava.GuavaCache
import com.twitter.util.Duration._
import com.twitter.util.Future

import scala.collection.mutable

class Medias extends CachedRepository {
  private[this] val medias = mutable.Map.empty[UUID, Array[Byte]]

  val fn: (UUID) => Future[Option[Array[Byte]]] = new ((UUID) => Future[Option[Array[Byte]]]) {
    @traced("fills-cache", "MutableMap", fromSeconds(1))
    override def apply(v1: UUID): Future[Option[Array[Byte]]] = Future.value(medias.synchronized(medias.get(v1)))
  }

  @traced("medias#get", "MutableMap", fromMilliseconds(800))
  def get(id: UUID): Future[Option[Array[Byte]]] = {
    val loadingCache: (UUID) => Future[Option[Array[Byte]]] = GuavaCache.fromLoadingCache(gCache)
    loadingCache(id)
  }

  @traced("medias#add", "MutableMap", fromMilliseconds(800))
  def add(data: Media): Future[UUID] = {
    Future.value(medias.synchronized {
      val copy: Media = data.copy(id = UUID.randomUUID())
      medias(copy.id) = toBytes(copy)
      copy.id
    })
  }
}

