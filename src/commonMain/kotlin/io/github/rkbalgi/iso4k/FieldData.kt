package io.github.rkbalgi.iso4k

import io.github.rkbalgi.iso4k.charsets.Charsets
import io.github.rkbalgi.iso4k.io.newBuffer
import io.ktor.utils.io.core.*

data class FieldData(val field: IsoField, private val data: ByteArray) {
  fun encodeToString(): String {
    var rawData = data()
    if (field.type == FieldType.Variable) {
      rawData = rawData.sliceArray((field.len) until rawData.size)
    }
    return Charsets.toString(rawData, field.dataEncoding)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as FieldData

    if (field != other.field) return false
    if (!data.contentEquals(other.data)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = field.hashCode()
    result = 31 * result + data.contentHashCode()
    return result
  }

  fun data(): ByteArray {
    return if (field.type == FieldType.Variable) {
      val buf = newBuffer(100)
      var pos: Int
      buf.apply {
        writeFully(buildLengthIndicator(field.lengthEncoding!!, field.len, data.size))
        writeFully(data)
        pos = buf.writePosition
        resetForRead()
      }

      buf.readBytes(pos)
    } else if (field.type == FieldType.Bitmapped) {
      if (data.sliceArray(16..23) contentEquals ByteArray(8)) {
        if (data.sliceArray(8..15) contentEquals ByteArray(8)) {
          data.sliceArray(0..7)
        } else {
          data.sliceArray(0..15)
        }
      } else {
        data
      }
    } else {
      data
    }
  }
}
