package art

import kotlin.test.Test
import kotlin.test.assertEquals

class Contents {
    @Test
    fun should_be_able_to_get_the_toc_indexed_by_headers() {
        val doc = document {
            h1("Introduction")
            p("This is an introduction")
            h1("Bottles")
            p("Some bottles are")
            h2("Plastic bottles")
            h2("Glass bottles")
            h1("Bags")
            h2("Plastic Blags")
            h2("Paper bags")
        }

        val toc = doc.toc()
        val expected = listOf(
            TOCItem(1, "1", "Introduction", 0),
            TOCItem(1, "2", "Bottles", 0),
            TOCItem(2, "2.1", "Plastic bottles", 1),
            TOCItem(2, "2.2", "Glass bottles", 1),
            TOCItem(1, "3", "Bags", 0),
            TOCItem(2, "3.1", "Plastic Blags", 1),
            TOCItem(2, "3.2", "Paper bags", 1)
        )
        assertEquals(expected, toc)
    }
}