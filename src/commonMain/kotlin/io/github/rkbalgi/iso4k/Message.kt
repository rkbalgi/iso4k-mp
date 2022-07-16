package io.github.rkbalgi.iso4k

import io.github.rkbalgi.iso4k.charsets.Charsets
import io.github.rkbalgi.iso4k.io.newBuffer
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class Message(val messageSegment: MessageSegment) {

    private var bitmap: IsoBitmap = IsoBitmap(ByteArray(24), null, null)
    internal val fieldDataMap: MutableMap<IsoField, FieldData> = mutableMapOf();

    fun fieldData(field: IsoField, fieldData: FieldData) {
        fieldDataMap[field] = fieldData
    }

    fun fieldData(fieldName: String, fieldValue: String) {
        val field = fieldByName(fieldName) ?: throw RuntimeException("No field found with name - $fieldName")
        fieldDataMap[field] = FieldData(field, Charsets.fromString(fieldValue, field.dataEncoding))
    }

    fun fieldData(fieldName: String, fieldValue: ByteArray) {
        val field = fieldByName(fieldName) ?: throw RuntimeException("No field found with name - $fieldName")
        fieldDataMap[field] = FieldData(field, fieldValue)
    }

    internal fun fieldByName(fieldName: String): IsoField? {
        messageSegment.fields.forEach {
            if (it.name == fieldName) {
                return it
            } else {
                val field = findInChildren(it, fieldName)
                if (field != null) {
                    return field
                }
            }
        }
        return null
    }

    private fun findInChildren(isoField: IsoField, fieldName: String): IsoField? {

        if (isoField.hasChildren()) {
            isoField.children?.forEach {
                if (it.name == fieldName) {
                    return it
                }
                val field = findInChildren(it, fieldName)
                if (field != null) {
                    return field
                }

            }
        }
        return null
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

    fun encodeToJson(): JsonObject {

        return buildJsonObject {
            messageSegment.fields.forEach {
                if (fieldDataMap.containsKey(it)) {
                    put(it.name, fieldAsJson(it))
                }
            }
        }
    }

    private fun fieldAsJson(isoField: IsoField): JsonElement {

        if (!isoField.hasChildren()) {
            return JsonPrimitive(fieldDataMap[isoField]?.encodeToString())
        }

        return buildJsonObject {

            put(isoField.name, JsonPrimitive(fieldDataMap[isoField]?.encodeToString()))
            put("subFields", buildJsonObject {
                isoField.children?.forEach { subField ->
                    if (isoField.type == FieldType.Bitmapped && bitmap.field == isoField) {
                        if (bitmap.isOn(subField.position)) {
                            put(subField.name, fieldAsJson(subField))
                        }
                    } else {
                        // other kinds of parent-child fields
                        if (fieldDataMap.containsKey(subField)) {
                            put(subField.name, fieldAsJson(subField))
                        }
                    }

                }
            })


        }
    }

    /**
     * Converts the msg to a ISO8583 ByteArray
     * @return A binary ByteArray representing the ISO8583 message
     */
    fun bytes(): ByteArray {

        val outBuf = newBuffer()
        messageSegment.fields.forEach {
            appendFieldData(outBuf, it)
        }
        return outBuf.readBytes(outBuf.writePosition)
    }

    private fun appendFieldData(outBuf: Buffer, field: IsoField) {
        var data = field.fieldData(this)
        if (data != null) {
            outBuf.writeFully(data)
        }

    }


}