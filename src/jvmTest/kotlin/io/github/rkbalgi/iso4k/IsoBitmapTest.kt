package io.github.rkbalgi.iso4k

import kotlin.test.Test
import kotlin.test.assertEquals


internal class IsoBitmapTest {


    @Test
    fun testBitmap() {

        val bmpData = fromHexString("f02420000000100e00000001000000010000000000000000")
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

        println(bmp.bytes().size)

        assertEquals("f02420000000100e00000001000000010000000000000000", bmp.bytes().toHexString())

    }

    @Test
    fun testBitmapCreate() {

        val bmp = IsoBitmap()
        listOf<Int>(1, 2, 3, 4, 11, 14, 19, 52, 61, 62, 63, 96, 128).forEach { bmp.setOn(it) }
        val res = bmp.bytes().toHexString()
        assertEquals("f02420000000100e00000001000000010000000000000000", res)


    }

}