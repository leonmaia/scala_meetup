package com.meetup.persistence

import com.meetup.Media
import com.meetup.json.toBytes
import com.rigon.zipkin.traced
import com.twitter.util.Duration._
import com.twitter.util.Future

import scala.collection.mutable

class Medias {
  private[this] val medias = mutable.Map.empty[Long, Array[Byte]]

  @traced("medias#get", "MutableMap", fromMilliseconds(800))
  def get(id: Long): Future[Array[Byte]] = Future(
    medias.synchronized {
      medias.getOrElse(id, throw new IllegalStateException("Media doesn't exist! :("))
    }
  )

  @traced("medias#add", "MutableMap", fromMilliseconds(800))
  def add(data: Media): Future[Long] = {
    Future.value(medias.synchronized {
      val nextId = if (medias.isEmpty) 0 else medias.keys.max + 1
      medias(nextId) = toBytes(data)
      nextId
    })
  }

  @traced("medias#all", "MutableMap", fromMilliseconds(800))
  def all: Future[Seq[Array[Byte]]] = Future.value(medias.synchronized(medias.toList.sortBy(_._1).map(_._2)))
}

