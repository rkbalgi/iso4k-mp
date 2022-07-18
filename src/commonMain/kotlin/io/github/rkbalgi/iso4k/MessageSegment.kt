package io.github.rkbalgi.iso4k

import io.github.rkbalgi.iso4k.io.newBuffer

/**
 * A MessageSegment defines the layout of structure of a message (a request or a response etc)
 *
 */
@kotlinx.serialization.Serializable
class MessageSegment(
        val id: Int,
        val name: String,
        private val selectors: List<String>,
        val fields: List<IsoField>
) {

    private var spec: Spec? = null

    fun spec(): Spec? {
        return spec
    }


    fun selectorMatch(selector: String): Boolean {
        return try {
            selectors.first { it == selector }
            true
        } catch (e: NoSuchElementException) {
            false
        }
    }

    fun parse(msgData: ByteArray): Message {
        val msg = Message(this)
        val msgBuf = newBuffer(msgData)
        fields.forEach { it.parse(msg, msgBuf) }
        return msg
    }

    fun setSpec(spec: Spec) {
        this.spec = spec
    }

    /**
     * Builds and returns a new blank Message
     *
     * @return A new blank message
     */
    fun blankMessage(): Message {
        val msg = Message(this)
        var bitmapField =
            this.fields.first { it -> it.type == FieldType.Bitmapped } //There can only be one top level field of type Bitmapped ??
        msg.bitmap(IsoBitmap(ByteArray(24), bitmapField, msg))

        return msg

    }

}
