package io.github.rkbalgi.iso4k

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*

actual fun newBuffer(data: ByteArray): Buffer {
    return Buffer(Memory.of(data)).also { it.resetForRead() }
}