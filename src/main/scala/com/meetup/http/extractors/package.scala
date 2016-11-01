package com.meetup.http

package object extractors {

  object UUID {
    def unapply(arg: String): Option[java.util.UUID] = Option(java.util.UUID.fromString(arg))
  }
}
