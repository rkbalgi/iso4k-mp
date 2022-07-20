package io.github.rkbalgi.iso4k.io

import io.ktor.utils.io.core.*

/** @return A new Buffer backed by a ByteArray for reading */
expect fun newBuffer(data: ByteArray): Buffer

/** @return A new Buffer for writing with specified size */
expect fun newBuffer(size: Int = 1024): Buffer
