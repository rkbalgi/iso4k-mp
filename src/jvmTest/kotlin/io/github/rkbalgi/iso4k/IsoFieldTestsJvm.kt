package io.github.rkbalgi.iso4k

import kotlin.test.Test
import kotlin.test.assertContentEquals


internal class IsoFieldTestsJvm {

    @Test
    fun testBuildAsciiLengthIndicator() {

        var res = buildLengthIndicator(DataEncoding.ASCII, 2, 18)
        assertContentEquals(arrayOf<Byte>(0x31, 0x38).toByteArray(), res)

        res = buildLengthIndicator(DataEncoding.ASCII, 3, 18)
        assertContentEquals(arrayOf<Byte>(0x30, 0x31, 0x38).toByteArray(), res)

        res = buildLengthIndicator(DataEncoding.ASCII, 3, 118)
        assertContentEquals(arrayOf<Byte>(0x31, 0x31, 0x38).toByteArray(), res)

        res = buildLengthIndicator(DataEncoding.ASCII, 3, 1)
        assertContentEquals(arrayOf<Byte>(0x30, 0x30, 0x31).toByteArray(), res)


    }


    @Test
    fun testBuildEbcdicLengthIndicator() {


        var res = buildLengthIndicator(DataEncoding.EBCDIC, 2, 18)
        assertContentEquals(fromHexString("f1f8"), res)

        res = buildLengthIndicator(DataEncoding.EBCDIC, 3, 18)
        assertContentEquals(fromHexString("f0f1f8"), res)

        res = buildLengthIndicator(DataEncoding.EBCDIC, 3, 118)
        assertContentEquals(fromHexString("f1f1f8"), res)

        res = buildLengthIndicator(DataEncoding.EBCDIC, 3, 1)
        assertContentEquals(fromHexString("f0f0f1"), res)


    }

    @Test
    fun testBuildBinaryLengthIndicator() {


        var res = buildLengthIndicator(DataEncoding.BINARY, 2, 18)
        assertContentEquals(fromHexString("0012"), res)

        res = buildLengthIndicator(DataEncoding.BINARY, 1, 18)
        assertContentEquals(fromHexString("12"), res)


    }

    @Test()
    fun testBuildBcdLengthIndicator() {


        var res = buildLengthIndicator(DataEncoding.BCD, 2, 18)
        assertContentEquals(fromHexString("0018"), res)

        res = buildLengthIndicator(DataEncoding.BCD, 1, 18)
        assertContentEquals(fromHexString("18"), res)


    }
}