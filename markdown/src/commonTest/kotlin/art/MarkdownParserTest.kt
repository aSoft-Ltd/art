package art

import kommander.expect
import kotlin.test.Test

class MarkdownParserTest {
    @Test
    fun should_be_able_to_parse_a_simple_heading() {
        val md = "# Introduction\n"
        val elements = md.parseMarkdown()
        expect(elements.size).toBe(1)
        val heading = elements[0] as Heading
        expect(heading.level).toBe(1)
        expect(heading.span.text).toBe("Introduction")
    }

    @Test
    fun should_be_able_to_parse_an_unordered_list() {
        val md = """
            - Item 1
            - Item 2
        """.trimIndent()
        val elements = md.parseMarkdown()
        expect(elements.size).toBe(1)
        val list = elements[0] as Bullets
        expect(list.items.size).toBe(2)
        expect(list.items[0][0].text).toBe("Item 1")
        expect(list.items[1][0].text).toBe("Item 2")
    }

    @Test
    fun should_be_able_to_parse_an_ordered_list() {
        val md = """
            1. Item 1
            2. Item 2
        """.trimIndent()
        val elements = md.parseMarkdown()
        expect(elements.size).toBe(1)
        val list = elements[0] as Sequence
        expect(list.items.size).toBe(2)
        expect(list.items[0][0].text).toBe("Item 1")
        expect(list.items[1][0].text).toBe("Item 2")
    }

    @Test
    fun should_be_able_to_parse_inline_styles() {
        val md = "This is **bold**, *italic*, `code`, and ~~strike~~."
        val elements = md.parseMarkdown()
        expect(elements.size).toBe(1)
        val p = elements[0] as Paragraph
        expect(p.spans.size).toBe(9)
        expect(p.spans[1].text).toBe("bold")
        expect(p.spans[1].style?.bold).toBe(true)
        expect(p.spans[3].text).toBe("italic")
        expect(p.spans[3].style?.italic).toBe(true)
        expect(p.spans[5].text).toBe("code")
        expect(p.spans[5].style?.quote).toBe(true)
        expect(p.spans[7].text).toBe("strike")
        expect(p.spans[7].style?.strike).toBe(true)
    }

    @Test
    fun should_be_able_to_parse_links() {
        val md = "[Google](https://www.google.com)"
        val elements = md.parseMarkdown()
        expect(elements.size).toBe(1)
        val p = elements[0] as Paragraph
        expect(p.spans[0].text).toBe("Google")
        expect(p.spans[0].link).toBe("https://www.google.com")
    }

    @Test
    fun should_be_able_to_parse_a_table() {
        val md = """
            | Name | Age |
            | --- | --- |
            | Alice | 30 |
        """.trimIndent()
        val elements = md.parseMarkdown()
        expect(elements.size).toBe(1)
        val table = elements[0] as Table
        expect(table.columns.size).toBe(2)
        expect(table.columns[0].spans[0].text).toBe("Name")
        expect(table.rows.size).toBe(1)
        expect(table.rows[0].cells[0][0].text).toBe("Alice")
    }

    @Test
    fun reverse_test_from_markdown_test() {
        val doc = document {
            h1("Introduction")
            p("This is a test document")
            ul {
                li("Item 1")
                li("Item 2")
            }
        }
        val markdown = doc.toMarkdown()
        val parsed = markdown.parseMarkdown()
        
        // Re-generate markdown from parsed and check if it's the same
        expect(parsed.toMarkdown()).toBe(markdown)
    }
}
