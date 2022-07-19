# iso4k-mp

A multi-platform (JVM/JS) library to work with ISO8583 messages built using Kotlin

Glossary -

1. A spec or specification defines message segments and header fields
2. A message segment is a layout of a ISO8583 request or a response
3. The parsed header determines the message segment to be used
4. A transaction a instance of a "message" composed of a request and a response

## Demo/Examples

### JS/Browser

See https://github.com/rkbalgi/iso4k-mp-browser-example

### JVM

The library reads a file specs.yml from the classpath root. This specs.yml defines a list of spec files (yaml files)
that
are also present in the classpath (see io.github.rkbalgi.iso4k.MiscTests.yamlTest for example)

```kotlin


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

```

```

{
    "message_type": "1100",
    "bitmap": {
        "bitmap": "f02420000000100e00000001000000010000000000000000",
        "subFields": {
            "pan": "4567909845671235",
            "proc_code": {
                "proc_code": "004000",
                "subFields": {
                    "df3.transaction_type": "00",
                    "df3.from_account": "40",
                    "df3.to_account": "00"
                }
            },
            "amount": "000000000029",
            "stan": "779581",
            "expiration_date": "2204",
            "country_code": "840",
            "pin_data": "77fcbd9ffc0dfa6f",
            "private_1": "reserved_1",
            "private_2": "reserved-2",
            "private_3": "87877622525",
            "key_mgmt_data": "1234",
            "mac_2": "e47006f5de8c70b9"
        }
    }
}
{
    "message_type": "1110",
    "bitmap": {
        "bitmap": "f02420000600100e00000001000000010000000000000000",
        "subFields": {
            "pan": "4567909845671235",
            "proc_code": {
                "proc_code": "004000",
                "subFields": {
                    "df3.transaction_type": "00",
                    "df3.from_account": "40",
                    "df3.to_account": "00"
                }
            },
            "amount": "000000000029",
            "stan": "779581",
            "expiration_date": "2204",
            "country_code": "840",
            "approval_code": "AP1234",
            "action_code": "000",
            "pin_data": "77fcbd9ffc0dfa6f",
            "private_1": "reserved_1",
            "private_2": "reserved-2",
            "private_3": "87877622525",
            "key_mgmt_data": "1234",
            "mac_2": "e47006f5de8c70b9"
        }
    }
}
Assembled Trace => 31313130f02420000600100e000000010000000131363435363739303938343536373132333530303430303030303030303030303030323937373935383132323034f8f4f041503132333430303077fcbd9ffc0dfa6f001072657365727665645f310a9985a28599a5858460f2f0f1f1383738373736323235323531323334e47006f5de8c70b9

----------Assembled Message --------
{
    "message_type": "1110",
    "bitmap": {
        "bitmap": "f02420000600100e00000001000000010000000000000000",
        "subFields": {
            "pan": "4567909845671235",
            "proc_code": {
                "proc_code": "004000",
                "subFields": {
                    "df3.transaction_type": "00",
                    "df3.from_account": "40",
                    "df3.to_account": "00"
                }
            },
            "amount": "000000000029",
            "stan": "779581",
            "expiration_date": "2204",
            "country_code": "840",
            "approval_code": "AP1234",
            "action_code": "000",
            "pin_data": "77fcbd9ffc0dfa6f",
            "private_1": "reserved_1",
            "private_2": "reserved-2",
            "private_3": "87877622525",
            "key_mgmt_data": "1234",
            "mac_2": "e47006f5de8c70b9"
        }
    }
}
BUILD SUCCESSFUL in 6s
5 actionable tasks: 3 executed, 2 up-to-date
1:32:31 PM: Execution finished ':cleanJvmTest :jvmTest --tests "io.github.rkbalgi.iso4k.CommonTests.test_message_parsing"'.

```

## Creating a new Message from scratch

You can also create a message from scratch without using a trace, like this -

```kotlin
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
```
