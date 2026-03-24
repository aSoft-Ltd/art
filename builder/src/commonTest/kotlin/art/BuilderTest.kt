package art

import kotlin.test.Test

class BuilderTest {
    @Test
    fun should_be_able_to_create_a_document() {
        val doc = document {
            h1("Introduction")
        }
    }
}