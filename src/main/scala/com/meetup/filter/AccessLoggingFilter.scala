package com.meetup.filter

import java.util.TimeZone

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.{Duration, Future, Stopwatch, Time}
import org.apache.commons.lang.time.FastDateFormat
import org.slf4j.LoggerFactory

object AccessLoggingFilter {
  def apply = new AccessLoggingFilter()
}

class AccessLoggingFilter() extends SimpleFilter[Request, Response] {

  val DateFormat = FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z", TimeZone.getTimeZone("GMT"))

  def logger = LoggerFactory.getLogger(this.getClass)

  def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val elapsed = Stopwatch.start()
    service(request) onSuccess { resp =>
      logger.info(format2(request, resp, elapsed()))
    }
  }

  def formattedDate(): String = DateFormat.format(Time.now.toDate)

  private[this] def format2(request: Request, response: Response, time: Duration): String = {
    val remoteAddr = request.remoteAddress.getHostAddress
    val identd = "- -"
    val httpVersion = request.version.toString
    val httpVerb = request.method.toString().toUpperCase
    val requestURI = request.uri
    val statusCode = response.statusCode
    val contentLength = response.length
    val contentLengthStr = if (contentLength > 0) contentLength.toString else "-"
    val uaStr = request.userAgent.getOrElse("-")
    f"""
       |$remoteAddr%s $identd%s [${formattedDate()}%s]
       | "$httpVerb%s $requestURI%s $httpVersion%s"
       | $statusCode%s
       | $contentLengthStr%s
       | ${time.inMilliseconds}%s
       | "$uaStr"
       |""".stripMargin.replaceAll("\n", "")
  }
}
