package io.github.rkbalgi.iso4k

import io.ktor.utils.io.*

actual fun ByteReadChannel.blockingReadAvailable(data: ByteArray) {
}

actual fun ByteArray.toHexString(): String {
    TODO("Not yet implemented")
}

actual fun fromHexString(str: String): ByteArray {
    TODO("Not yet implemented")
}

actual fun ByteReadChannel.blockingReadAvailable(
    data: ByteArray,
    offset: Int,
    len: Int
) {
}