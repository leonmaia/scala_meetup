package com.meetup.http

import com.meetup.{EvalInput, Media}
import com.meetup.json._
import com.twitter.finagle.http.Request

package object readers {
  val evalInput = new ((Request) => EvalInput) {
    override def apply(v1: Request): EvalInput = fromJson[EvalInput](v1.contentString)
  }

  val transmission = new ((Request) => Media) {
    override def apply(v1: Request): Media = fromJson[Media](v1.contentString)
  }
}
