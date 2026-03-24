package art

import kommander.expect
import kotlin.test.Test

class MarkdownTest {
    @Test
    fun should_be_able_to_create_a_document() {
        val doc = document {
            h1("Introduction")
        }
        expect(doc.toMarkdown()).toBe("# Introduction\n")
    }

    @Test
    fun should_be_able_to_generate_markdown_of_an_unordered_list() {
        val doc = document {
            h1("Introduction")
            p("This is a test document")
            ul {
                li("Item 1")
                li("Item 2")
            }
        }
        expect(doc.toMarkdown()).toBe("# Introduction\nThis is a test document\n- Item 1\n- Item 2\n")
    }

    @Test
    fun should_be_able_to_generate_markdown_of_an_ordered_list() {
        val doc = document {
            h1("Introduction")
            p("This is a test document")
            ol {
                li("Item 1")
                li("Item 2")
            }
        }
        expect(doc.toMarkdown()).toBe("# Introduction\nThis is a test document\n1. Item 1\n2. Item 2\n")
    }
}