package io.github.rkbalgi.iso4k

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import net.mamoe.yamlkt.Yaml
import kotlin.collections.set

@JsModule("specs")
@JsNonModule
external fun allSpecs(): Any?

/**
 * Loads spec definitions. The JS implementation uses inline YAML spec definitions or in future call
 * an external HTTP API
 *
 * @return List of available specs
 */
actual fun loadSpecs(): List<String>? {

    Napier.base(DebugAntilog())

    var fromJS = allSpecs() as List<String>
    for (spec in fromJS) {
        println(spec)
    }

    Napier.i("Loading Spec definitions .. ")

    var decoded = Yaml.decodeFromString(
        Spec.serializer(), """

name: SampleSpec
id: 3
requestResponseMTIMapping:
  - requestMTI: 1100
    responseMTI: 1110
  - requestMTI: 1420
    responseMTI: 1430

headerFields:
  - name: "hdr_msg_type"
    id: 1
    type: Fixed
    len: 4
    dataEncoding: ASCII
    children: []
messageSegments:
  - name: "1100/1110 - Authorization"
    selectors:
      - "1100"
      - "1110"
    id: 1
    fields:
      - name: "message_type"
        id: 1
        type: Fixed
        len: 4
        dataEncoding: ASCII

      - name: "bitmap"
        id: 2
        type: Bitmapped
        len: 0
        dataEncoding: BINARY
        children:
          - name: "pan"
            id: 3
            type: Variable
            len: 2
            lengthEncoding: ASCII
            dataEncoding: ASCII
            position: 2

          - name: "proc_code"
            id: 4
            type: Fixed
            len: 6
            dataEncoding: ASCII
            position: 3

          - name: "amount"
            id: 8
            type: Fixed
            len: 12
            dataEncoding: ASCII
            position: 4

          - name: "stan"
            id: 9
            type: Fixed
            len: 6
            dataEncoding: ASCII
            key: true
            position: 11

          - name: "expiration_date"
            id: 16
            type: Fixed
            len: 4
            dataEncoding: ASCII
            position: 14

          - name: "country_code"
            id: 17
            type: Fixed
            len: 3
            dataEncoding: EBCDIC
            position: 19

          - name: "approval_code"
            id: 10
            type: Fixed
            len: 6
            dataEncoding: ASCII
            position: 38

          - name: "action_code"
            id: 11
            type: Fixed
            len: 3
            dataEncoding: ASCII
            position: 39

          - name: "pin_data"
            id: 12
            type: Fixed
            len: 8
            dataEncoding: BINARY
            position: 52

          - name: "private_1"
            id: 18
            type: Variable
            len: 2
            lengthEncoding: BCD
            dataEncoding: ASCII
            position: 61

          - name: "private_2"
            id: 19
            type: Variable
            len: 1
            lengthEncoding: BINARY
            dataEncoding: EBCDIC
            position: 62

          - name: "private_3"
            id: 20
            type: Variable
            len: 3
            lengthEncoding: EBCDIC
            dataEncoding: ASCII
            position: 63

          - name: "mac_1"
            id: 21
            type: Fixed
            len: 8
            dataEncoding: BINARY
            position: 64

          - name: "key_mgmt_data"
            id: 14
            type: Fixed
            len: 4
            dataEncoding: ASCII
            position: 96

          - name: "mac_2"
            id: 22
            type: Fixed
            len: 8
            dataEncoding: BINARY
            position: 128

          - name: "reserved_data"
            id: 14
            type: Fixed
            len: 4
            dataEncoding: ASCII
            position: 160

  - name: "1420/1430 - Reversal"
    selectors:
      - "1420"
      - "1430"
    id: 2
    fields:
      - name: "message_type"
        id: 1
        type: Fixed
        len: 4
        dataEncoding: ASCII

      - name: "bitmap"
        id: 2
        type: Bitmapped
        len: 0
        dataEncoding: BINARY
        children:
          - name: "pan"
            id: 3
            type: Variable
            len: 2
            lengthEncoding: ASCII
            dataEncoding: ASCII
            position: 2

          - name: "proc_code"
            id: 4
            type: Fixed
            len: 6
            dataEncoding: ASCII
            position: 3

          - name: "amount"
            id: 8
            type: Fixed
            len: 12
            dataEncoding: ASCII
            position: 4

          - name: "stan"
            id: 9
            type: Fixed
            len: 6
            dataEncoding: ASCII
            key: true
            position: 11

          - name: "expiration_date"
            id: 16
            type: Fixed
            len: 4
            dataEncoding: ASCII
            position: 14

          - name: "country_code"
            id: 17
            type: Fixed
            len: 3
            dataEncoding: EBCDIC
            position: 19

          - name: "approval_code"
            id: 10
            type: Fixed
            len: 6
            dataEncoding: ASCII
            position: 38

          - name: "action_code"
            id: 11
            type: Fixed
            len: 3
            dataEncoding: ASCII
            position: 39

          - name: "private_3"
            id: 20
            type: Variable
            len: 3
            lengthEncoding: EBCDIC
            dataEncoding: ASCII
            position: 63

          - name: "key_mgmt_data"
            id: 14
            type: Fixed
            len: 4
            dataEncoding: ASCII
            position: 96

          - name: "reserved_data"
            id: 14
            type: Fixed
            len: 4
            dataEncoding: ASCII
            position: 160




    """.trimIndent()
    )

    Spec.specMap[decoded.name] = decoded
    return listOf(decoded.name)
}

