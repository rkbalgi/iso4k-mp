# iso4k-mp
A multi-platform (JVM/JS) library to work with ISO8583 messages built using Kotlin

The goal of this project is to provide a library like JPOS which can then be integrated with other applications like https://github.com/rkbalgi/keedoh or https://github.com/rkbalgi/tcptester


The specifications will be defined in yaml files and will be borrowed from my other project (https://github.com/rkbalgi/iso8583_rs/blob/master/sample_spec/sample_spec.yaml)

Glossary -
1. A spec or specification defines message segments and header fields
2. A message segment is a layout of a ISO8583 request or a response
3. The parsed header determines the message segment to be used
4. A transaction a instance of a "message" composed of a request and a response


# Sample (WIP)
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
C:\Users\rkbal\DevTools\zulu14.27.1-ca-jdk14-win_x64\bin\java.exe -ea -Didea.test.cyclic.buffer.size=1048576 -javaagent:C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\lib\idea_rt.jar=59683:C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\bin -Dfile.encoding=UTF-8 -classpath C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\lib\idea_rt.jar;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\junit\lib\junit5-rt.jar;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\junit\lib\junit-rt.jar;C:\Users\rkbal\IdeaProjects\iso4k\target\test-classes;C:\Users\rkbal\IdeaProjects\iso4k\target\classes;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\Kotlin\kotlinc\lib\kotlin-stdlib.jar;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\Kotlin\kotlinc\lib\kotlin-reflect.jar;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\Kotlin\kotlinc\lib\kotlin-test.jar;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\Kotlin\kotlinc\lib\kotlin-stdlib-jdk7.jar;C:\Users\rkbal\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\203.4449.2\plugins\Kotlin\kotlinc\lib\kotlin-stdlib-jdk8.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-stdlib\1.4.0\kotlin-stdlib-1.4.0.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-stdlib-common\1.4.0\kotlin-stdlib-common-1.4.0.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\annotations\13.0\annotations-13.0.jar;C:\Users\rkbal\.m2\repository\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;C:\Users\rkbal\.m2\repository\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;C:\Users\rkbal\.m2\repository\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;C:\Users\rkbal\.m2\repository\com\fasterxml\jackson\dataformat\jackson-dataformat-yaml\2.11.1\jackson-dataformat-yaml-2.11.1.jar;C:\Users\rkbal\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.11.1\jackson-databind-2.11.1.jar;C:\Users\rkbal\.m2\repository\org\yaml\snakeyaml\1.26\snakeyaml-1.26.jar;C:\Users\rkbal\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.11.1\jackson-core-2.11.1.jar;C:\Users\rkbal\.m2\repository\com\fasterxml\jackson\module\jackson-module-kotlin\2.11.1\jackson-module-kotlin-2.11.1.jar;C:\Users\rkbal\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.11.1\jackson-annotations-2.11.1.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-reflect\1.3.72\kotlin-reflect-1.3.72.jar;C:\Users\rkbal\.m2\repository\com\google\guava\guava\29.0-jre\guava-29.0-jre.jar;C:\Users\rkbal\.m2\repository\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;C:\Users\rkbal\.m2\repository\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;C:\Users\rkbal\.m2\repository\com\google\code\findbugs\jsr305\3.0.2\jsr305-3.0.2.jar;C:\Users\rkbal\.m2\repository\org\checkerframework\checker-qual\2.11.1\checker-qual-2.11.1.jar;C:\Users\rkbal\.m2\repository\com\google\errorprone\error_prone_annotations\2.3.4\error_prone_annotations-2.3.4.jar;C:\Users\rkbal\.m2\repository\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-test-junit\1.4.0\kotlin-test-junit-1.4.0.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-test-annotations-common\1.4.0\kotlin-test-annotations-common-1.4.0.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-test\1.4.0\kotlin-test-1.4.0.jar;C:\Users\rkbal\.m2\repository\org\jetbrains\kotlin\kotlin-test-common\1.4.0\kotlin-test-common-1.4.0.jar;C:\Users\rkbal\.m2\repository\junit\junit\4.12\junit-4.12.jar;C:\Users\rkbal\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\rkbal\.m2\repository\org\junit\jupiter\junit-jupiter\5.7.0\junit-jupiter-5.7.0.jar;C:\Users\rkbal\.m2\repository\org\junit\jupiter\junit-jupiter-api\5.7.0\junit-jupiter-api-5.7.0.jar;C:\Users\rkbal\.m2\repository\org\apiguardian\apiguardian-api\1.1.0\apiguardian-api-1.1.0.jar;C:\Users\rkbal\.m2\repository\org\opentest4j\opentest4j\1.2.0\opentest4j-1.2.0.jar;C:\Users\rkbal\.m2\repository\org\junit\platform\junit-platform-commons\1.7.0\junit-platform-commons-1.7.0.jar;C:\Users\rkbal\.m2\repository\org\junit\jupiter\junit-jupiter-params\5.7.0\junit-jupiter-params-5.7.0.jar;C:\Users\rkbal\.m2\repository\org\junit\jupiter\junit-jupiter-engine\5.7.0\junit-jupiter-engine-5.7.0.jar;C:\Users\rkbal\.m2\repository\org\junit\platform\junit-platform-engine\1.7.0\junit-platform-engine-1.7.0.jar com.intellij.rt.junit.JUnitStarter -ideVersion5 -junit4 com.github.rkbalgi.MiscTests,yamlTest
02:23:49.519 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field message_type: data(raw): 31313030 data(encoded): 1100
02:23:49.524 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field pan: data(raw): 34353637393039383435363731323335 data(encoded): 4567909845671235
02:23:49.525 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field proc_code: data(raw): 303034303030 data(encoded): 004000
02:23:49.526 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field amount: data(raw): 303030303030303030303239 data(encoded): 000000000029
02:23:49.527 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field stan: data(raw): 373739353831 data(encoded): 779581
02:23:49.528 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field expiration_date: data(raw): 32323034 data(encoded): 2204
02:23:49.549 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field country_code: data(raw): f8f4f0 data(encoded): 840
02:23:49.550 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field pin_data: data(raw): 77fcbd9ffc0dfa6f data(encoded): 77fcbd9ffc0dfa6f
02:23:49.551 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field private_1: data(raw): 72657365727665645f31 data(encoded): reserved_1
02:23:49.552 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field private_2: data(raw): 9985a28599a5858460f2 data(encoded): reserved-2
02:23:49.553 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field private_3: data(raw): 3837383737363232353235 data(encoded): 87877622525
02:23:49.553 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field key_mgmt_data: data(raw): 31323334 data(encoded): 1234
02:23:49.555 [main] DEBUG com.github.rkbalgi.iso4k.IsoField - field mac_2: data(raw): e47006f5de8c70b9 data(encoded): e47006f5de8c70b9


```