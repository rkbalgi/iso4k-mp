package io.github.rkbalgi.iso4k.io

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*

expect fun newBuffer(data: ByteArray): Buffer