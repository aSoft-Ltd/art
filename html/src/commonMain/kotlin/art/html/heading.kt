package art.html

import art.Heading
import art.TOCItem
import art.tooling.slug
import kotlinx.html.FlowContent
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.h5
import kotlinx.html.h6
import kotlinx.html.id
import kotlinx.html.style

internal fun FlowContent.heading(
    h: Heading,
    numbers: Boolean = false,
    toc: List<TOCItem>
) {
    val prefix = if (numbers) {
        toc.find { it.text == h.span.text }?.prefix
    } else null

    val text = if (prefix != null) "$prefix. ${h.span.text}" else h.span.text

    when (h.level) {
        1 -> h1 {
            id = h.span.text.slug()
            style = "padding-top:0.5rem"
            +text
        }

        2 -> h2 {
            id = h.span.text.slug()
            style = "padding-top:0.5rem"
            +text
        }

        3 -> h3 {
            id = h.span.text.slug()
            style = "padding-top:0.5rem"
            +text
        }

        4 -> h4 {
            id = h.span.text.slug()
            style = "padding-top:0.5rem"
            +text
        }

        5 -> h5 {
            id = h.span.text.slug()
            style = "padding-top:0.5rem"
            +text
        }

        else -> h6 {
            id = h.span.text.slug()
            style = "padding-top:0.5rem"
            +text
        }
    }
}