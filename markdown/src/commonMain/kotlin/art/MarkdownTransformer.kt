package art

import kotlin.jvm.JvmName

fun List<Element>.toMarkdown(): String = buildString {
    for (element in this@toMarkdown) {
        when (element) {
            is Heading -> appendLine("#".repeat(element.level) + " " + element.span.text + "\n")

            is Paragraph -> appendLine(element.spans.toMarkdown())

            is Bullets -> for (item in element.items) {
                appendLine("- ${item.toMarkdown()}")
            }

            is Sequence -> for ((index, item) in element.items.withIndex()) {
                appendLine("${index + 1}. ${item.toMarkdown()}")
            }

            else -> error("Unsupported element: $element")
        }
    }
}

@JvmName("toMarkdownSpans")
private fun List<Span>.toMarkdown(): String = buildString {
    for (span in this@toMarkdown) when {
        span.style?.bold == true -> append("**${span.markdown}**")
        span.style?.italic == true -> append("*${span.markdown}*")
        span.style?.quote == true -> append("`${span.markdown}`")
        span.style?.strike == true -> append("~~${span.markdown}~~")
        else -> append(span.markdown)
    }
}

private val Span.markdown get() = if (link != null) "[${text}](${link})" else text