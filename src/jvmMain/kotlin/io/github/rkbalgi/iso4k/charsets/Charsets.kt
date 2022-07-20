package io.github.rkbalgi.iso4k.charsets

import io.github.rkbalgi.iso4k.DataEncoding
import io.github.rkbalgi.iso4k.fromHexString
import io.github.rkbalgi.iso4k.toHexString
import java.nio.ByteBuffer
import java.nio.charset.Charset

actual class Charsets {

  actual companion object {

    actual fun toString(data: ByteArray, encoding: DataEncoding): String {
      return when (encoding) {
        DataEncoding.ASCII -> {
          Charset.forName("US-ASCII").decode(ByteBuffer.wrap(data)).toString()
        }
        DataEncoding.EBCDIC -> {
          Charset.forName("cp037").decode(ByteBuffer.wrap(data)).toString()
        }
        DataEncoding.BCD,
        DataEncoding.BINARY -> {
          data.toHexString()
        }
      }
    }

    actual fun fromString(data: String, encoding: DataEncoding): ByteArray {
      return when (encoding) {
        DataEncoding.ASCII -> {
          data.toByteArray(Charset.forName("US-ASCII"))
        }
        DataEncoding.EBCDIC -> {
          data.toByteArray(Charset.forName("cp037"))
        }
        DataEncoding.BCD,
        DataEncoding.BINARY -> {
          fromHexString(data)
        }
      }
    }
  }
}
