package com.meetup.filter

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future

object CacheFilter {
  def apply(value: String): SimpleFilter[Request, Response] = new CacheFilter().cached(value)
}

private class CacheFilter {
  def cached(value: String): SimpleFilter[Request, Response] = new SimpleFilter[Request, Response] {
    def apply(req: Request, service: Service[Request, Response]): Future[Response] = {
      service(req) onSuccess ( _.cacheControl_=(value))
    }
  }
}
