package art

import art.html.bullets
import art.html.heading
import art.html.paragraph
import art.html.sequence
import art.html.span
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.Art(
    content: List<Element>,
    numbers: Boolean = false,
    classes: Set<String> = emptySet(),
    style: String? = null
) = div {
    this.classes = classes
    if (style != null) {
        this.style = style
    }
    val toc = if (numbers) {
        content.toc()
    } else {
        emptyList()
    }
    for (element in content) when (element) {
        is Bullets -> bullets(element, padding = "1em")
        is Heading -> heading(element, numbers, toc)
        is Image -> +"<<<<<<<<< Image here >>>>>>>>>"
        is Paragraph -> paragraph(element)
        is Sequence -> sequence(element, padding = "1em")
        is Span -> span(element)
        is Table -> +"<<<<<<<<< table here >>>>>>>>>"
    }
}