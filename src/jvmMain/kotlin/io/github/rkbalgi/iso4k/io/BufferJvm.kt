package io.github.rkbalgi.iso4k.io

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import java.nio.ByteBuffer

actual fun newBuffer(data: ByteArray): Buffer {
  return Buffer(Memory.of(data)).also { it.resetForRead() }
}

actual fun newBuffer(size: Int): Buffer {
  return Buffer(Memory.of(ByteBuffer.allocate(size))).also { it.resetForWrite() }
}
