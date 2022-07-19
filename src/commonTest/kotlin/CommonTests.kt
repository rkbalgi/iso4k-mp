package io.github.rkbalgi.iso4k

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*

class CommonTests {


    @BeforeTest
    fun testInit() {
        addSpecsForTests()
    }

    @Test
    fun test_create_msg_scratch() {

        val spec = Spec.spec("SampleSpec")!!
        val msgSegment = spec.message("1100/1110 - Authorization")!!

        val msg = msgSegment.blankMessage()
        msg.apply {
            fieldData("message_type", "1100")
            bitmap().apply {
                setOn(3, "002000")
                setOn(4, "000000002200")
                setOn(96, "ABCD")
                setOn(64, "01020304abcdef12")
            }
        }
        assertEquals(
            "31313030b000000000000001000000010000000030303230303030303030303030303232303001020304abcdef1241424344",
            msg.bytes().toHexString()
        )
        println(Json { prettyPrint = true }.encodeToString(msg.encodeToJson()))


    }

    @Test
    fun test_message_parsing() {

        val spec = Spec.spec("SampleSpec")

        val msgData =
            fromHexString("31313030f02420000000100e000000010000000131363435363739303938343536373132333530303430303030303030303030303030323937373935383132323034f8f4f077fcbd9ffc0dfa6f001072657365727665645f310a9985a28599a5858460f2f0f1f1383738373736323235323531323334e47006f5de8c70b9")
        val msg = spec?.message("1100/1110 - Authorization")?.parse(msgData)


        //
        val jsonConfig = Json {
            prettyPrint = true
        }
        msg.run {
            assertNotNull(this)
            println(Json { prettyPrint = true }.encodeToString(encodeToJson()))

            assertEquals("1100", this.get("message_type")?.encodeToString())
            assertEquals("004000", this.bitmap().get(3)?.encodeToString())
            assertEquals("reserved_1", this.bitmap().get(61)?.encodeToString())
        }

        // changing the msg
        msg.run {

            assertNotNull(this)
            // changing the msg
            fieldData("message_type", "1110")
            bitmap().setOn(38, "AP1234")
            bitmap().setOn(39, "000")
            println("Assembled Trace => " + msg?.bytes()?.toHexString())
            println(Json { prettyPrint = true }.encodeToString(encodeToJson()))

        }


        // parse the assembled message and assert
        val assembledMsg = spec?.message("1100/1110 - Authorization")?.parse(msg!!.bytes())
        println("----------Assembled Message --------\n" + jsonConfig.encodeToString(assembledMsg?.encodeToJson()))
        assertEquals(msg?.encodeToJson(), assembledMsg?.encodeToJson())

    }

    @Test
    fun test_field_parent_linkup() {

        //loadSpecs()
        var msg = Spec.spec("SampleSpec")?.message("1100/1110 - Authorization")
        assertNotNull(msg)

        msg.fields.first { it.name == "bitmap" }.apply {
            var child = children?.first { it.name == "proc_code" }
            assertTrue { child?.parent == this }
        }


    }
}