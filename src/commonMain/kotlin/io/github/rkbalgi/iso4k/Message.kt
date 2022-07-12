package io.github.rkbalgi.iso4k

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

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


}