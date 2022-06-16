package io.github.rkbalgi.iso4k


import io.github.aakira.napier.Napier
import io.github.rkbalgi.iso4k.charsets.Charsets
import io.ktor.utils.io.*
import kotlin.experimental.and

@kotlinx.serialization.Serializable
enum class DataEncoding {
    ASCII, BCD, BINARY, EBCDIC
}

@kotlinx.serialization.Serializable
enum class FieldType {
    Fixed, Variable, Terminated, Bitmapped
}
@kotlinx.serialization.Serializable
data class IsoField(
        val id: Int,
        val name: String,
        val type: FieldType,
        val len: Int,
        val dataEncoding: DataEncoding,
        val lengthEncoding: DataEncoding?,
        val children: Array<IsoField>?,
        var position: Int = 0,
        var key: Boolean = false,

        ) {


    fun hasChildren(): Boolean {
        return children != null && children.isNotEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        } else {
            if (other is IsoField) {
                return this.id == other.id && this.name == other.name
            }
        }

        return false
    }

    override fun hashCode(): Int {
        return id.hashCode() + name.hashCode()
    }


}

data class FieldData(val field: IsoField, val data: ByteArray) {
    fun encodeToString(): String {
        return Charsets.toString(data, field.dataEncoding)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FieldData

        if (field != other.field) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}


fun IsoField.parse(msg: Message, buf: ByteReadChannel) {

    when (type) {
        FieldType.Fixed -> parseFixed(this, msg, buf)
        FieldType.Variable -> parseVariable(this, msg, buf)
        FieldType.Bitmapped -> parseBitmapped(this, msg, buf)
        FieldType.Terminated -> TODO("No support terminated fields")
    }

}

expect fun ByteReadChannel.blockingReadAvailable(data: ByteArray)
expect fun ByteReadChannel.blockingReadAvailable(data: ByteArray, offset: Int, len: Int)
expect fun ByteArray.toHexString(): String
expect fun fromHexString(str: String): ByteArray


fun IsoField.parse(buf: ByteReadChannel): String {

    when (type) {
        FieldType.Fixed -> {

            val data = ByteArray(len)
            buf.blockingReadAvailable(data)
            return FieldData(this, data).encodeToString()
        }


        else -> TODO("only fixed type supported for header fields")
    }

}


fun Byte.isHighBitSet(): Boolean {
    return (this and 0x80.toByte()) == 0x80.toByte()
}

private fun parseBitmapped(field: IsoField, msg: Message, buf: ByteReadChannel) {

    when (field.dataEncoding) {
        DataEncoding.BINARY -> {

            val bmpData = ByteArray(24);

            buf.blockingReadAvailable(bmpData, 0, 8)
            if (bmpData[0].isHighBitSet()) {
                //secondary bitmap present
                bmpData.sliceArray(IntRange(0, 7))
                buf.blockingReadAvailable(bmpData, 8, 8)
                if (bmpData[8].isHighBitSet()) {
                    //tertiary also present
                    buf.blockingReadAvailable(bmpData, 16, 8)
                }
            }
            msg.setBitmap(IsoBitmap(bmpData, field, msg))
            setAndLog(msg, FieldData(field, bmpData))
            field.children?.filter { it.position > 0 && msg.bitmap().isOn(it.position) }?.forEach { it.parse(msg, buf) }
        }
        else -> {
            TODO("bitmap unimplemented for encoding type: $field.dataEncoding")
        }
    }
}

private fun parseFixed(field: IsoField, msg: Message, buf: ByteReadChannel) {


    val data = ByteArray(field.len)
    buf.blockingReadAvailable(data)
    val fieldData = FieldData(field, data)
    setAndLog(msg, fieldData)


    if (field.hasChildren()) {
        field.children?.forEach {
            //TODO:: can rewind and try without a fresh allocation
            val newBuf = ByteReadChannel(data)
            it.parse(msg, newBuf)
        }
    }

}

internal fun setAndLog(msg: Message, fieldData: FieldData) {
    msg.setFieldData(fieldData.field, fieldData)
    Napier.d(
        "field ${fieldData.field.name}: data(raw): ${fieldData.data.toHexString()} data(encoded): ${fieldData.encodeToString()}"
    )

}


fun parseVariable(field: IsoField, msg: Message, buf: ByteReadChannel) {


    val len = readFieldLength(field, buf)
    val data = ByteArray(len)

    buf.blockingReadAvailable(data)
    val fieldData = FieldData(field, data)
    setAndLog(msg, fieldData)


    if (field.hasChildren()) {
        val newBuf = ByteReadChannel(data)
        field.children?.forEach {
            it.parse(msg, newBuf)
        }
    }

}

internal fun readFieldLength(field: IsoField, buffer: ByteReadChannel): Int {
    val tmp = ByteArray(field.len)
    buffer.blockingReadAvailable(tmp)

    if (field.lengthEncoding == DataEncoding.BINARY) {
        return Charsets.toString(tmp, field.lengthEncoding).toInt(16)
    }

    return Charsets.toString(tmp, field.lengthEncoding!!).toInt()

}

internal fun buildLengthIndicator(encoding: DataEncoding, len: Int, dataLength: Int): ByteArray {

    return when (encoding) {
        DataEncoding.BCD -> {
            Charsets.fromString(dataLength.toString().padStart((len * 2), '0'), encoding)
        }
        DataEncoding.BINARY -> {
            Charsets.fromString(dataLength.toString(16).padStart((len * 2), '0'), encoding)
        }
        DataEncoding.ASCII, DataEncoding.EBCDIC -> {
            Charsets.fromString(dataLength.toString().padStart(len, '0'), encoding)
        }
    }
}
