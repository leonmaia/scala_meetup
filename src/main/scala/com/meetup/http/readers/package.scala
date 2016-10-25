package com.meetup.http

import com.meetup.json.converters._
import com.twitter.finagle.http.Request

package object readers {

  case class EvalInput(expr: String)

  val evalInput = new ((Request) => EvalInput) {
    override def apply(v1: Request): EvalInput = v1.contentString.fromJson[EvalInput]
  }
}
