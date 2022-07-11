package io.github.rkbalgi.iso4k.charsets

import io.github.rkbalgi.iso4k.fromHexString

var ascii2ebcdic = mutableMapOf<Int, Int>()
var ebcdic2ascii = mutableMapOf<Int, Int>()

var init = false;

const val ASCII: String = "ASCII"
const val EBCDIC: String = "EBCDIC"

/**
 * Converts a [ByteArray] to a [String] in the given charset (ASCII/EBCDIC)
 * @receiver ByteArray
 * @param charset the charset to be used
 *
 * @return String encoded to charset
 */
fun ByteArray.encodedString(charset: String): String {

    return when (charset) {
        ASCII -> io.ktor.utils.io.core.String(this, 0, this.size, io.ktor.utils.io.charsets.Charsets.UTF_8)
        EBCDIC -> io.ktor.utils.io.core.String(convertE2A(this), 0, this.size, io.ktor.utils.io.charsets.Charsets.UTF_8)
        else -> throw IllegalArgumentException("No such charset - $charset")
    }
}

/**
 * Converts a [String] to a [ByteArray] as per the provided charset
 * @receiver String
 * @param charset the charset to be used
 */
fun String.toBytes(charset: String): ByteArray {
    return when (charset) {
        ASCII -> this.encodeToByteArray()
        EBCDIC -> convertA2E(this.encodeToByteArray())
        else -> throw IllegalArgumentException("No such charset - $charset")
    }
}

internal fun convertA2E(data: ByteArray): ByteArray {

    if (!init) {
        initMaps()
    }

    var res = ByteArray(data.size)
    var i = 0

    data.forEach {

        if (ascii2ebcdic.containsKey(it.toInt() and 0xff)) {
            res[i++] = ascii2ebcdic[it.toInt() and 0xff]!!.toByte()
        } else {
            res[i++] = '?'.code.toByte()
        }
    }
    return res

}

internal fun convertE2A(data: ByteArray): ByteArray {

    if (!init) {
        initMaps()
    }

    var res = ByteArray(data.size)
    var i = 0

    data.forEach {

        if (ebcdic2ascii.containsKey(0xff and it.toInt())) {
            res[i++] = ebcdic2ascii[it.toInt() and 0xff]!!.toByte()
        } else {
            res[i++] = '?'.code.toByte()
        }
    }
    return res

}

// only converts letters and numbers
// TODO:: add supports for other characters
internal fun initMaps() {

    ascii2ebcdic[0x30] = 0xf0;
    ascii2ebcdic[0x31] = 0xf1;
    ascii2ebcdic[0x32] = 0xf2;
    ascii2ebcdic[0x33] = 0xf3;
    ascii2ebcdic[0x34] = 0xf4;
    ascii2ebcdic[0x35] = 0xf5;
    ascii2ebcdic[0x36] = 0xf6;
    ascii2ebcdic[0x37] = 0xf7;
    ascii2ebcdic[0x38] = 0xf8;
    ascii2ebcdic[0x39] = 0xf9;
    ascii2ebcdic[0x61] = 0x81;
    ascii2ebcdic[0x62] = 0x82;
    ascii2ebcdic[0x63] = 0x83;
    ascii2ebcdic[0x64] = 0x84;
    ascii2ebcdic[0x65] = 0x85;
    ascii2ebcdic[0x66] = 0x86;
    ascii2ebcdic[0x67] = 0x87;
    ascii2ebcdic[0x68] = 0x88;
    ascii2ebcdic[0x69] = 0x89;
    ascii2ebcdic[0x6a] = 0x91;
    ascii2ebcdic[0x6b] = 0x92;
    ascii2ebcdic[0x6c] = 0x93;
    ascii2ebcdic[0x6d] = 0x94;
    ascii2ebcdic[0x6e] = 0x95;
    ascii2ebcdic[0x6f] = 0x96;
    ascii2ebcdic[0x70] = 0x97;
    ascii2ebcdic[0x71] = 0x98;
    ascii2ebcdic[0x72] = 0x99;
    ascii2ebcdic[0x73] = 0xa2;
    ascii2ebcdic[0x74] = 0xa3;
    ascii2ebcdic[0x75] = 0xa4;
    ascii2ebcdic[0x76] = 0xa5;
    ascii2ebcdic[0x77] = 0xa6;
    ascii2ebcdic[0x78] = 0xa7;
    ascii2ebcdic[0x79] = 0xa8;
    ascii2ebcdic[0x7a] = 0xa9;
    ascii2ebcdic[0x41] = 0xc1;
    ascii2ebcdic[0x42] = 0xc2;
    ascii2ebcdic[0x43] = 0xc3;
    ascii2ebcdic[0x44] = 0xc4;
    ascii2ebcdic[0x45] = 0xc5;
    ascii2ebcdic[0x46] = 0xc6;
    ascii2ebcdic[0x47] = 0xc7;
    ascii2ebcdic[0x48] = 0xc8;
    ascii2ebcdic[0x49] = 0xc9;
    ascii2ebcdic[0x4a] = 0xd1;
    ascii2ebcdic[0x4b] = 0xd2;
    ascii2ebcdic[0x4c] = 0xd3;
    ascii2ebcdic[0x4d] = 0xd4;
    ascii2ebcdic[0x4e] = 0xd5;
    ascii2ebcdic[0x4f] = 0xd6;
    ascii2ebcdic[0x50] = 0xd7;
    ascii2ebcdic[0x51] = 0xd8;
    ascii2ebcdic[0x52] = 0xd9;
    ascii2ebcdic[0x53] = 0xe2;
    ascii2ebcdic[0x54] = 0xe3;
    ascii2ebcdic[0x55] = 0xe4;
    ascii2ebcdic[0x56] = 0xe5;
    ascii2ebcdic[0x57] = 0xe6;
    ascii2ebcdic[0x58] = 0xe7;
    ascii2ebcdic[0x59] = 0xe8;
    ascii2ebcdic[0x5a] = 0xe9;


//ebcdic to ascii map
    ebcdic2ascii[0xf0] = 0x30;
    ebcdic2ascii[0xf1] = 0x31;
    ebcdic2ascii[0xf2] = 0x32;
    ebcdic2ascii[0xf3] = 0x33;
    ebcdic2ascii[0xf4] = 0x34;
    ebcdic2ascii[0xf5] = 0x35;
    ebcdic2ascii[0xf6] = 0x36;
    ebcdic2ascii[0xf7] = 0x37;
    ebcdic2ascii[0xf8] = 0x38;
    ebcdic2ascii[0xf9] = 0x39;
    ebcdic2ascii[0x81] = 0x61;
    ebcdic2ascii[0x82] = 0x62;
    ebcdic2ascii[0x83] = 0x63;
    ebcdic2ascii[0x84] = 0x64;
    ebcdic2ascii[0x85] = 0x65;
    ebcdic2ascii[0x86] = 0x66;
    ebcdic2ascii[0x87] = 0x67;
    ebcdic2ascii[0x88] = 0x68;
    ebcdic2ascii[0x89] = 0x69;
    ebcdic2ascii[0x91] = 0x6a;
    ebcdic2ascii[0x92] = 0x6b;
    ebcdic2ascii[0x93] = 0x6c;
    ebcdic2ascii[0x94] = 0x6d;
    ebcdic2ascii[0x95] = 0x6e;
    ebcdic2ascii[0x96] = 0x6f;
    ebcdic2ascii[0x97] = 0x70;
    ebcdic2ascii[0x98] = 0x71;
    ebcdic2ascii[0x99] = 0x72;
    ebcdic2ascii[0xa2] = 0x73;
    ebcdic2ascii[0xa3] = 0x74;
    ebcdic2ascii[0xa4] = 0x75;
    ebcdic2ascii[0xa5] = 0x76;
    ebcdic2ascii[0xa6] = 0x77;
    ebcdic2ascii[0xa7] = 0x78;
    ebcdic2ascii[0xa8] = 0x79;
    ebcdic2ascii[0xa9] = 0x7a;
    ebcdic2ascii[0xc1] = 0x41;
    ebcdic2ascii[0xc2] = 0x42;
    ebcdic2ascii[0xc3] = 0x43;
    ebcdic2ascii[0xc4] = 0x44;
    ebcdic2ascii[0xc5] = 0x45;
    ebcdic2ascii[0xc6] = 0x46;
    ebcdic2ascii[0xc7] = 0x47;
    ebcdic2ascii[0xc8] = 0x48;
    ebcdic2ascii[0xc9] = 0x49;
    ebcdic2ascii[0xd1] = 0x4a;
    ebcdic2ascii[0xd2] = 0x4b;
    ebcdic2ascii[0xd3] = 0x4c;
    ebcdic2ascii[0xd4] = 0x4d;
    ebcdic2ascii[0xd5] = 0x4e;
    ebcdic2ascii[0xd6] = 0x4f;
    ebcdic2ascii[0xd7] = 0x50;
    ebcdic2ascii[0xd8] = 0x51;
    ebcdic2ascii[0xd9] = 0x52;
    ebcdic2ascii[0xe2] = 0x53;
    ebcdic2ascii[0xe3] = 0x54;
    ebcdic2ascii[0xe4] = 0x55;
    ebcdic2ascii[0xe5] = 0x56;
    ebcdic2ascii[0xe6] = 0x57;
    ebcdic2ascii[0xe7] = 0x58;
    ebcdic2ascii[0xe8] = 0x59;
    ebcdic2ascii[0xe9] = 0x5a;

    // other printable chars
    val otherCharsAscii = fromHexString( "202122232425262728292a2b2c2d2e2f3a3b3c3d3e3f405b5c5d5e5f607b7c7d7e")
    val otherCharsEbcdic = fromHexString("405a7f7b5b6c507d4d5d5c4e6b604b617a5e4c7e6e6f7cbae0bbb06d79c04fd0a1")
    otherCharsAscii.forEachIndexed { i, b ->
        ascii2ebcdic[0xff and (b as Int)] = 0xff and (otherCharsEbcdic[i] as Int)
    }
    otherCharsEbcdic.forEachIndexed { i, b ->
        ebcdic2ascii[0xff and (b as Int)] = 0xff and (otherCharsAscii[i] as Int)
    }




    init = true
}