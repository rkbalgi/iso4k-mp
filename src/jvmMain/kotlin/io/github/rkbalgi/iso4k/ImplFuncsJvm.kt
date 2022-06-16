package io.github.rkbalgi.iso4k

import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.bouncycastle.util.encoders.Hex

/**
 * Returns a hex-string representing the contents of the [ByteArray]
 * @receiver ByteArray
 * @return the hex string
 */
actual fun ByteArray.toHexString(): String =
    this.joinToString("") { (0xff and it.toInt()).toString(16).padStart(2, '0') }


/**
 * @param str A string containing hex characters
 * @return ByteArray
 */
actual fun fromHexString(str: String): ByteArray {
    return Hex.decode(str)!!
}


actual fun ByteReadChannel.blockingReadAvailable(data: ByteArray) {
    runBlocking { readAvailable(data) }
}

actual fun ByteReadChannel.blockingReadAvailable(data: ByteArray, offset: Int, len: Int) {
    runBlocking {
        readAvailable(data, offset, len)
    }
}
