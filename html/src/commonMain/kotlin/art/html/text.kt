package art.html

import art.Paragraph
import art.Span
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.span
import kotlinx.html.style

internal fun FlowContent.span(s: Span) {
    val styles = buildList {
        if (s.style?.bold == true) add("font-weight:bold")
        if (s.style?.italic == true) add("font-style:italic")
        if (s.style?.underline == true) add("text-decoration:underline")
        if (s.style?.strike == true) add("text-decoration:line-through")
        if (s.style?.quote == true) add("background-color: rgba(80,80,80,80,0.5); padding: 0.1em 0.2em; border-radius: 0.2em")
    }
    if (s.link != null) a(href = s.link) {
        style = styles.joinToString(";")
        +s.text
    } else span {
        style = styles.joinToString(";")
        +s.text
    }
}

internal fun FlowContent.paragraph(p: Paragraph) {
    for (span in p.spans) span(span)
}