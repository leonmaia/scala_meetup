package com.meetup.json

import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers.{wrappedBuffer => toBuffer}

package object converters {
  implicit class Unmarshallable(unMarshallMe: String) {
    def toMap: Map[String,Any] = JsonUtil.toMap(unMarshallMe)
    def toMapOf[V]()(implicit m: Manifest[V]): Map[String,V] = JsonUtil.toMap[V](unMarshallMe)
    def fromJson[T]()(implicit m: Manifest[T]): T =  JsonUtil.fromJson[T](unMarshallMe)
  }

  implicit class Marshallable[T](marshallMe: T) {
    def toJson: String = JsonUtil.toJson(marshallMe)
    def toBytes: Array[Byte] = JsonUtil.toBytes(marshallMe)
  }

  def serialize(obj: Any): ChannelBuffer = {
    obj match {
      case s: String => toBuffer(s.getBytes)
      case a: Array[Byte] => toBuffer(a)
      case _ => toBuffer(obj.toBytes)
    }
  }
}