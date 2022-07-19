package io.github.rkbalgi.iso4k

import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IsoBitmapTests {


    @Test
    fun testBitmap() {

        val bmpData = fromHexString("f02420000000100e00000001000000010000000000000000")
        println(bmpData.toHexString())
        val bmp = IsoBitmap(bmpData, null, null)

        for (i in 1..192) {
            if (bmp.isOn(i)) {
                println("$i is enabled")
            }
        }


        assertEquals(true, bmp.isOn(1))
        assertEquals(true, bmp.isOn(4))
        assertEquals(true, bmp.isOn(14))
        assertEquals(false, bmp.isOn(64))
        assertEquals(true, bmp.isOn(96))
        assertEquals(true, bmp.isOn(61))
        assertEquals(true, bmp.isOn(128))

        println(bmp.bytes().toHexString())

        assertEquals("f02420000000100e00000001000000010000000000000000", bmp.bytes().toHexString())

    }

    @Test
    fun testBitmapCreate() {

        val bmp = IsoBitmap()
        listOf<Int>(1, 2, 3, 4, 11, 14, 19, 52, 61, 62, 63, 96, 128).forEach { bmp.setOn(it) }
        val res = bmp.bytes().toHexString()
        assertEquals("f02420000000100e00000001000000010000000000000000", res)


    }

    @Test
    @Ignore
    fun test_turning_off_bits() {

        val bmp = IsoBitmap()
        listOf<Int>(2, 3, 4, 11, 14, 19, 52, 61, 62, 63, 96, 128).forEach { bmp.setOn(it) }
        val res = bmp.bytes().toHexString()
        assertEquals("f02420000000100e00000001000000010000000000000000", res)

        var bmpfield =
            IsoField(id = 1, name = "bitmap", dataEncoding = DataEncoding.BINARY, type = FieldType.Bitmapped, len = 24)
        bmp.field = bmpfield


        bmp.setOff(96)
        assertEquals("f02420000000100e00000000000000010000000000000000", bmp.bytes().toHexString())
        assertEquals("f02420000000100e0000000000000001", FieldData(bmpfield, bmp.bytes()).encodeToString())

        bmp.setOff(128)
        assertEquals("702420000000100e00000000000000000000000000000000", bmp.bytes().toHexString())


        assertEquals("702420000000100e", FieldData(bmpfield, bmp.bytes()).encodeToString())


    }

}