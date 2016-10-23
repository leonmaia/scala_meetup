package com.meetup.http

import com.meetup.json.converters._
import com.twitter.finagle.http.{Response, Status, Version}

object Responses {
  def respond(obj: Any, status: Status = Status.Ok): Response = {
    val res = Response(Version.Http11, status)
    res.setContentTypeJson()
    res.write(serialize(obj))

    res
  }

  def respondWithStatus(status: Status = Status.Ok): Response = {
    Response(Version.Http11, status)
  }
}
