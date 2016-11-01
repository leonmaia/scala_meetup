package com.meetup.http

import com.meetup.EvalInput
import com.meetup.json._
import com.twitter.finagle.http.Request

import scala.util.{Failure, Success, Try}

package object readers {
  val evalInput = new ((Request) => EvalInput) {
    override def apply(v1: Request): EvalInput = fromJson[EvalInput](v1.contentString)
  }

  def body[T](request: Request)(implicit m: Manifest[T]): T = {
    request.contentString match {
      case x if x.isEmpty => throw new IllegalArgumentException("Required body not present in the request.")
      case _ =>
        val triedT: Try[T] = Try(fromJson[T](request.contentString))
        triedT match {
          case Failure(f) => throw new IllegalArgumentException("Invalid json.")
          case Success(t) => t
        }
    }
  }
}
