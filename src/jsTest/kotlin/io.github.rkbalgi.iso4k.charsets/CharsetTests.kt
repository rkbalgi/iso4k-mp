package io.github.rkbalgi.iso4k.charsets

import io.github.rkbalgi.iso4k.DataEncoding
import io.github.rkbalgi.iso4k.fromHexString
import io.github.rkbalgi.iso4k.toHexString
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CharsetTests {

    @Test
    fun test_custom_encoding() {

        val ebcdic = fromHexString("F1F1F1F0C1")
        assertEquals("1110A", ebcdic.encodedString(EBCDIC))
        assertContentEquals(fromHexString("3131313041"), ebcdic.encodedString(EBCDIC).toBytes(ASCII))

        val str = "~~~~~~~~~~~~~"
        val ebcdic2 = Charsets.fromString(str, DataEncoding.EBCDIC)
        println(ebcdic2.toHexString())

        assertEquals(str, ebcdic2.encodedString(EBCDIC))

    }
}
