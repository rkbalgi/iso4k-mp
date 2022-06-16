package io.github.rkbalgi.iso4k

import io.ktor.utils.io.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise


actual fun ByteArray.toHexString(): String =
    this.joinToString("") { (0xff and it.toInt()).toString(16).padStart(2, '0') }

actual fun fromHexString(str: String): ByteArray {


    check(str.length % 2 == 0)
    val res = ByteArray(str.length / 2)

    var i = 0
    str.chunked(2).forEach { a ->
        res[i++] = a.toInt(16).toByte()
    }
    return res
}


actual fun ByteReadChannel.blockingReadAvailable(data: ByteArray) {
    GlobalScope.promise { readAvailable(data) }
}

actual fun ByteReadChannel.blockingReadAvailable(data: ByteArray, offset: Int, len: Int) {
    GlobalScope.promise {
        readAvailable(data, offset, len)
    }
}