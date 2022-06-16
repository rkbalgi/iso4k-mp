package io.github.rkbalgi.iso4k.charsets;

import io.github.rkbalgi.iso4k.DataEncoding
import io.github.rkbalgi.iso4k.fromHexString
import io.github.rkbalgi.iso4k.toHexString
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*


actual class Charsets {


    actual companion object {
        actual fun toString(data: ByteArray, encoding: DataEncoding): String {
            return when (encoding) {
                DataEncoding.ASCII -> {
                    data.contentToString()
                }
                DataEncoding.EBCDIC -> {
                    data.encodedString(EBCDIC)
                }
                DataEncoding.BCD, DataEncoding.BINARY -> {
                    data.toHexString()
                }
            }
        }

        actual fun fromString(data: String, encoding: DataEncoding): ByteArray {
            return when (encoding) {
                DataEncoding.ASCII -> {
                    data.toByteArray(io.ktor.utils.io.charsets.Charsets.UTF_8)
                }
                DataEncoding.EBCDIC -> {
                    data.toBytes(EBCDIC)
                }
                DataEncoding.BCD, DataEncoding.BINARY -> {
                    fromHexString(data)
                }
            }
        }

    }
}