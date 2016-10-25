package com.meetup.server

import com.meetup.http.Responses._
import com.meetup.http.readers.evalInput
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Eval, Future}

class EvalService extends Service[Request, Response] {

  override def apply(request: Request): Future[Response] = Future.value(respond(Eval(evalInput(request).expr).toString))
}
