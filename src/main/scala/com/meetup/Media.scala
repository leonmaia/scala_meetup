package com.meetup

import java.util.UUID

case class Media(id: UUID, title: String, description: String, url: String) {
  require(title.nonEmpty)
  require(description.nonEmpty)
  require(url.nonEmpty)
}

