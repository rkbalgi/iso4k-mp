import io.github.rkbalgi.iso4k.charsets.ASCII
import io.github.rkbalgi.iso4k.charsets.EBCDIC
import io.github.rkbalgi.iso4k.charsets.encodedString
import io.github.rkbalgi.iso4k.charsets.toBytes
import io.github.rkbalgi.iso4k.fromHexString
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CharsetTests {

    @Test
    fun test_custom_encoding() {

        var ebcdic = fromHexString("F1F1F1F0C1")
        assertEquals("1110A", ebcdic.encodedString(EBCDIC))
        assertContentEquals(fromHexString("3131313041"), ebcdic.encodedString(EBCDIC).toBytes(ASCII))


    }
}
