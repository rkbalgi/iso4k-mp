import io.github.rkbalgi.iso4k.Spec
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SpecTestsJs {

    @Test
    fun test_field_parent_linkup() {

        //loadSpecs()
        var msg = Spec.spec("SampleSpec")?.message("1100/1110 - Authorization")
        assertNotNull(msg)

        msg.fields.first { it.name == "bitmap" }.apply {
            children?.forEach { println(it.name) }
            var child = children!!.first { it.name == "proc_code" }
            assertTrue { child.parent == this }
        }


    }
}