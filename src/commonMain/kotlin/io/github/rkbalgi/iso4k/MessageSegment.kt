package io.github.rkbalgi.iso4k

import io.github.rkbalgi.iso4k.io.newBuffer

/** A MessageSegment defines the layout of structure of a message (a request or a response etc) */
@kotlinx.serialization.Serializable
class MessageSegment(
    val id: Int,
    val name: String,
    private val selectors: List<String>,
    val fields: List<IsoField>
) {

  private var spec: Spec? = null

  /** @return The Spec to which this MessageSegment belongs */
  fun spec(spec: Spec) {
    this.spec = spec
  }

  fun selectorMatch(selector: String): Boolean {
    return try {
      selectors.first { it == selector }
      true
    } catch (e: NoSuchElementException) {
      false
    }
  }

  /**
   * Parse the given ByteArray as per the structure defined by the MessageSegment
   * @return The parsed Message
   */
  fun parse(msgData: ByteArray): Message {
    val msg = Message(this)
    val msgBuf = newBuffer(msgData)
    fields.forEach { it.parse(msg, msgBuf) }
    return msg
  }

  /**
   * Returns the BitMap defined for this MessageSegment
   * @return IsoBitmapField for this MessageSegment
   */
  fun bitmap(): IsoBitmapField {
    val bmpField = fields.first { it.type == FieldType.Bitmapped }
    return IsoBitmapField(bmpField)
  }

  /**
   * Builds and returns a new blank Message that can be used to build a Message from scratch
   *
   * @return A new blank message
   */
  fun blankMessage(): Message {
    val msg = Message(this)
    val bitmapField =
        this.fields.first { it ->
          it.type == FieldType.Bitmapped
        } // There can only be one top level field of type Bitmapped ??
    msg.bitmap(IsoBitmap(ByteArray(24), bitmapField, msg))

    return msg
  }
}
