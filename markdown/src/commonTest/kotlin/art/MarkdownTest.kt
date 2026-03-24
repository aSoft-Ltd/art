package art

import kommander.expect
import kotlin.test.Test

class MarkdownTest {
    @Test
    fun should_be_able_to_create_a_document() {
        val doc = document {
            h1("Introduction")
        }
        expect(doc.toMarkdown()).toBe(
            """
            # Introduction
            
            
        """.trimIndent()
        )
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
        expect(doc.toMarkdown()).toBe(
            """
            # Introduction
            
            This is a test document
            - Item 1
            - Item 2

        """.trimIndent()
        )
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
        expect(doc.toMarkdown()).toBe(
            """
            # Introduction
            
            This is a test document
            1. Item 1
            2. Item 2

        """.trimIndent()
        )
    }

    @Test
    fun should_be_able_to_have_inline_markdown_styles() {
        val doc = document {
            ol {
                li {
                    text("Welcome to ")
                    link("Google", "https://www.google.com")
                }
                li {
                    text("This text is ")
                    bold("bold")
                }

                li {
                    text("This text is ")
                    italic("italic")
                }

                li {
                    text("This text is ")
                    quoted("quoted")
                }

                li {
                    text("This text is ")
                    struck("struck through")
                }
            }
        }

        expect(doc.toMarkdown()).toBe(
            """
            1. Welcome to ~~[Google](https://www.google.com)~~
            2. This text is **bold**
            3. This text is *italic*
            4. This text is `quoted`
            5. This text is ~~struck through~~
            
        """.trimIndent()
        )
    }

    @Test
    fun should_be_able_to_render_a_table() {
        val doc = document {
            table {
                header {
                    cell("Name")
                    cell("Age")
                }
                row {
                    cell("Alice")
                    cell("30")
                }
            }
        }

        expect(doc.toMarkdown()).toBe(
            """
            | Name | Age |
            | --- | --- |
            | Alice | 30 |
            
            
        """.trimIndent()
        )
    }
}