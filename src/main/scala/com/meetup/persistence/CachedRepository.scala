package com.meetup.persistence

import java.util.UUID
import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import com.twitter.util.Future

trait CachedRepository {
  val gCache: LoadingCache[UUID, Future[Option[Array[Byte]]]] = {
    CacheBuilder.newBuilder()
      .maximumSize(10000)
      .expireAfterWrite(10, TimeUnit.MINUTES)
      .build(
        new CacheLoader[UUID, Future[Option[Array[Byte]]]] {
          override def load(cacheKey: UUID) = {
            fn(cacheKey)
          }
        }
      )
  }

  val fn: (UUID) => Future[Option[Array[Byte]]]
}

