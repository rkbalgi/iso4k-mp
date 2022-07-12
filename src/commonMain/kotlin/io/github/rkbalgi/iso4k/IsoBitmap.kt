package io.github.rkbalgi.iso4k

fun Long.toBytes(): ByteArray {

    var value = this
    val result = ByteArray(8)
    for (i in 7 downTo 0) {
        result[i] = ((value and 0xff).toByte())
        value = value shr 8
    }
    return result
}

fun Long.isBitOn(pos: Int): Boolean {
    return this.shr(64 - pos).and(0x01) == 0x01L
}

fun fromBytes(data: ByteArray): Long {

    return (((data[0].toLong() and 0xFFL) shl 56) or
            ((data[1].toLong() and 0xFFL) shl 48) or
            ((data[2].toLong() and 0xFFL) shl 40) or
            ((data[3].toLong() and 0xFFL) shl 32) or
            ((data[4].toLong() and 0xFFL) shl 24) or
            ((data[5].toLong() and 0xFFL) shl 16) or
            ((data[6].toLong() and 0xFFL) shl 8) or
            ((data[7].toLong() and 0xFF)))


}

class IsoBitmap(private val bmpData: ByteArray, var field: IsoField?, msg: Message?) {

    private var l1: Long = fromBytes(bmpData.sliceArray(0..7))
    private var l2: Long = fromBytes(bmpData.sliceArray(8..15))
    private var l3: Long = fromBytes(bmpData.sliceArray(16..23))

    private var msg: Message? = msg


    constructor() : this(ByteArray(24), null, null)

    fun isOn(pos: Int): Boolean {
        check(pos in 1..192)

        return when {
            pos < 65 -> {
                l1.isBitOn(pos)
            }
            pos < 129 -> {
                l2.isBitOn(pos - 64)
            }
            else -> {
                l3.isBitOn(pos - 128)
            }
        }
    }

    fun setOn(pos: Int) {
        check(pos in 1..192)
        val bp = when {
            pos < 65 -> {
                Triple(1, l1, 64 - pos)
            }
            pos < 129 -> {
                Triple(2, l2, 128 - pos)
            }
            else -> {
                Triple(3, l3, 192 - pos)
            }
        }

        var l: Long = 1
        l = l.shl(bp.third).or(bp.second)
        when (bp.first) {
            1 -> l1 = l
            2 -> {
                l2 = l
                setOn(1)
            }
            3 -> {
                l3 = l
                setOn(65)
            }
        }
    }

    /**
     * @return the bytes that make up the bitmap
     */
    fun bytes(): ByteArray {
        val bmpData = ByteArray(24)
        l1.toBytes().copyInto(bmpData, destinationOffset = 0)
        l2.toBytes().copyInto(bmpData, destinationOffset = 8)
        l3.toBytes().copyInto(bmpData, destinationOffset = 16)

        return bmpData
    }

    fun get(fieldName: String): FieldData? {

        try {
            val res = this.field!!.children?.first { it.name == fieldName }
            if (res != null && this.msg!!.fieldDataMap.containsKey(res)) {
                return this.msg!!.fieldDataMap[res]!!
            }

        } catch (e: NoSuchElementException) {
            throw NoSuchElementException("No field defined with name $fieldName")
        }

        return null


    }

    fun get(pos: Int): FieldData? {

        try {
            var res = this.field!!.children?.first { it.position == pos }
            if (this.msg!!.fieldDataMap.containsKey(res)) {
                return this.msg!!.fieldDataMap[res]!!
            }

        } catch (e: NoSuchElementException) {
            throw NoSuchElementException("No field defined @ position $pos")
        }

        return null
    }


}

class FieldNotPresentException(msg: String) : Throwable(msg)


