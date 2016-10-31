package com.meetup

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers._

package object json {
  implicit val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.setSerializationInclusion(Include.NON_NULL)
  mapper.enable(SerializationFeature.INDENT_OUTPUT)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  mapper.disable(DeserializationFeature.WRAP_EXCEPTIONS)
  mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

  implicit def fromJson[T](json: String)(implicit m: Manifest[T]): T = mapper.readValue[T](json)

  implicit def toJson(value: Any): String = mapper.writeValueAsString(value)

  implicit def toBytes(obj: Any): Array[Byte] = mapper.writeValueAsBytes(obj)

  implicit def serialize(obj: Any): ChannelBuffer = {
    obj match {
      case s: String => wrappedBuffer(s.getBytes)
      case a: Array[Byte] => wrappedBuffer(a)
      case _ => wrappedBuffer(toBytes(obj))
    }
  }
}
