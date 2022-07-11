package io.github.rkbalgi.iso4k

import io.github.rkbalgi.iso4k.Spec
import io.github.rkbalgi.iso4k.fromHexString
import java.nio.charset.Charset

import kotlin.test.Test


import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


internal class MiscTests {


    @Test
    fun findMessageTest() {

        val spec = Spec.spec("SampleSpec")

        val msgData = fromHexString("31313030")
        assertEquals("1100/1110 - Authorization", spec!!.findMessage(msgData))
        assertNull(spec.findMessage(fromHexString("31313131")))

    }

    @Test
    fun yamlTest() {

        val spec = Spec.spec("SampleSpec")

        val msgData =
            fromHexString("31313030f02420000000100e000000010000000131363435363739303938343536373132333530303430303030303030303030303030323937373935383132323034f8f4f077fcbd9ffc0dfa6f001072657365727665645f310a9985a28599a5858460f2f0f1f1383738373736323235323531323334e47006f5de8c70b9")
        val msg = spec?.message("1100/1110 - Authorization")?.parse(msgData)


        assertNotNull(msg)
        assertEquals("1100", msg.get("message_type")?.encodeToString())
        assertEquals("004000", msg.bitmap().get(3)?.encodeToString())
        assertEquals("reserved_1", msg.bitmap().get(61)?.encodeToString())


    }

    @Test
    fun test_mti_checks() {
        val spec = Spec.spec("SampleSpec")!!
        assertTrue { spec.isRequest("1100") }
        assertTrue { spec.isResponse("1110") }
        assertTrue { spec.responseMTI("1420") == "1430" }
    }


    @Test
    fun test_ebcdic2asii_mapping(){
        val data=fromHexString("2122232425262728292a2b2c2d2e2f3a3b3c3d3e3f405b5c5d5e5f607b7c7d7e")
        var str = String(data, Charsets.US_ASCII)
        var buf = Charset.forName("cp037").encode(str)

        println(str)
        println(buf.array().toHexString())


    }

}

