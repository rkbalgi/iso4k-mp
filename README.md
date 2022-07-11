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

The library reads a file specs.yml from the classpath root. This specs.yml defines a list of spec files (yaml files) that
are also present in the classpath (see src/test/resources for example)

```kotlin
        val spec = Spec.spec("SampleSpec")

        val msgData =
            fromHexString("31313030f02420000000100e000000010000000131363435363739303938343536373132333530303430303030303030303030303030323937373935383132323034f8f4f077fcbd9ffc0dfa6f001072657365727665645f310a9985a28599a5858460f2f0f1f1383738373736323235323531323334e47006f5de8c70b9")
        val msg = spec?.message("1100 - Authorization")?.parse(msgData)

        assertNotNull(msg)
        assertEquals("1100", msg.get("message_type").encodeToString())
        assertEquals("004000", msg.bitmap().get(3).encodeToString())
        assertEquals("reserved_1", msg.bitmap().get(61).encodeToString())
```

```
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
INFO: [INFO] LoadSpecsJvmKt$loadSpecs - Loading spec definitions from classpath
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
INFO: [INFO] LoadSpecsJvmKt$loadSpecs - Reading spec /sample_spec.yml from classpath
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] Spec$init - Initializing Spec - SampleSpec
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
INFO: [INFO] Spec$Companion$spec - Initialized Specs
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field message_type: data(raw): 31313030 data(encoded): 1100
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field bitmap: data(raw): f02420000000100e00000001000000010000000000000000 data(encoded): f02420000000100e00000001000000010000000000000000
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field pan: data(raw): 34353637393039383435363731323335 data(encoded): 4567909845671235
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field proc_code: data(raw): 303034303030 data(encoded): 004000
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field amount: data(raw): 303030303030303030303239 data(encoded): 000000000029
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field stan: data(raw): 373739353831 data(encoded): 779581
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field expiration_date: data(raw): 32323034 data(encoded): 2204
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field country_code: data(raw): f8f4f0 data(encoded): 840
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field pin_data: data(raw): 77fcbd9ffc0dfa6f data(encoded): 77fcbd9ffc0dfa6f
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field private_1: data(raw): 72657365727665645f31 data(encoded): reserved_1
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field private_2: data(raw): 9985a28599a5858460f2 data(encoded): reserved-2
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field private_3: data(raw): 3837383737363232353235 data(encoded): 87877622525
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field key_mgmt_data: data(raw): 31323334 data(encoded): 1234
Jul 11, 2022 1:21:54 PM io.github.aakira.napier.DebugAntilog performLog
FINE: [DEBUG] IsoFieldKt$setAndLog - field mac_2: data(raw): e47006f5de8c70b9 data(encoded): e47006f5de8c70b9


```
