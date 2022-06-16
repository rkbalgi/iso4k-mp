package io.github.rkbalgi.iso4k

class Message(val messageSegment: MessageSegment) {

    private var bitmap: IsoBitmap = IsoBitmap(ByteArray(24), null, null)
    internal val fieldDataMap: MutableMap<IsoField, FieldData> = mutableMapOf();

    internal fun setFieldData(field: IsoField, fieldData: FieldData) {
        fieldDataMap[field] = fieldData
    }

    /**
     * @return the bitmap associated with this message
     */
    fun bitmap(): IsoBitmap {
        return bitmap
    }

    /**
     * @param bitmap the bitmap to be used with this message
     */
    fun setBitmap(bitmap: IsoBitmap) {
        this.bitmap = bitmap
    }

    fun get(fieldName: String): FieldData? {
        val res = fieldDataMap.filter { it.key.name == fieldName }
        return if (res.isNotEmpty()) {
            res.values.first()
        } else {
            null
        }


    }


}