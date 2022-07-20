package io.github.rkbalgi.iso4k.charsets

import io.github.rkbalgi.iso4k.DataEncoding

expect class Charsets {

  companion object {

    fun toString(data: ByteArray, encoding: DataEncoding): String
    fun fromString(data: String, encoding: DataEncoding): ByteArray
  }
}
