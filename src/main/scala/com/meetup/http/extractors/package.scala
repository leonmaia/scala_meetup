package com.meetup.http

import scala.util.{Failure, Success, Try}

package object extractors {

  object UUID {

    def unapply(arg: String): Option[java.util.UUID] = {
      Try(java.util.UUID.fromString(arg)) match {
        case Success(uuid) => Option(uuid)
        case Failure(e: Exception) => throw e
      }
    }
  }

}
