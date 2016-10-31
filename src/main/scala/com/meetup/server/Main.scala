package com.meetup.server

import com.twitter.app.GlobalFlag
import com.twitter.conversions.storage.intToStorageUnitableWholeNumber
import com.twitter.finagle.{Http, param}
import com.twitter.server.{Lifecycle, LogFormat, TwitterServer}
import com.twitter.util.Await
import org.slf4j.LoggerFactory
import zipkin.finagle.http.HttpZipkinTracer

object maxRequestSize extends GlobalFlag[Int](5, "Max request size (in megabytes)")

object Main extends TwitterServer
  with Lifecycle
  with LogFormat {

  def logger = LoggerFactory.getLogger(this.getClass)

  def serviceName: String = "meetup-api-server"

  def port = ":8888"

  val router = new Router()

  def main(): Unit = {
    val name = s"http/$serviceName"

    System.setProperty("zipkin.initialSampleRate", "1.0")
    System.setProperty("zipkin.http.host", "localhost:9411")

    val server = Http.server
      .withTracer(new HttpZipkinTracer())
      .configured(param.Label(name))
      .configured(Http.param.MaxRequestSize(maxRequestSize().megabytes))
      .serve(port, router.create)
    onExit {
      server.close()
    }
    Await.ready(server)
  }
}

