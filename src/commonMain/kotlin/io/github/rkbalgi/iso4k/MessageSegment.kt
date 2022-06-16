package io.github.rkbalgi.iso4k

import io.ktor.utils.io.*

/**
 * A MessageSegment defines the layout of structure of a message (a request or a response etc)
 *
 */
@kotlinx.serialization.Serializable
class MessageSegment(
        val id: Int,
        val name: String,
        private val selectors: List<String>,
        private val fields: List<IsoField>
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
        val msgBuf = ByteReadChannel(msgData)
        fields.forEach { it.parse(msg, msgBuf) }
        return msg
    }

    fun setSpec(spec: Spec) {
        this.spec=spec
    }

}
