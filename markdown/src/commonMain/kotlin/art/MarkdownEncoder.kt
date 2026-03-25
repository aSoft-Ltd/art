package art

import kotlin.jvm.JvmName

fun List<Element>.toMarkdown(): String = buildString {
    for (element in this@toMarkdown) {
        when (element) {
            is Heading -> appendLine("#".repeat(element.level) + " " + element.span.text + "\n")

            is Paragraph -> {
                appendLine(element.spans.toMarkdown())
                appendLine()
            }

            is Bullets -> {
                for (item in element.items) {
                    appendLine("- ${item.toMarkdown()}")
                }
                appendLine()
            }

            is Sequence -> {
                for ((index, item) in element.items.withIndex()) {
                    appendLine("${index + 1}. ${item.toMarkdown()}")
                }
                appendLine()
            }

            is Table -> {
                appendLine(element.toMarkdown())
                appendLine()
            }

            else -> error("Unsupported element: $element")
        }
    }
}.trimEnd() + "\n"

private fun Table.toMarkdown(): String = buildString {
    if (rows.isEmpty() || columns.isEmpty()) return@buildString
    appendLine(columns.toMarkdownRow { it.spans.toMarkdown() })
    appendLine(columns.toMarkdownRow { "---" })
    for (row in rows) appendLine(row.toMarkdown())
}

private fun <T> List<T>.toMarkdownRow(transform: (T) -> String) = joinToString(prefix = "| ", separator = " | ", postfix = " |", transform = transform)

private fun Row.toMarkdown(): String = cells.toMarkdownRow { it.toMarkdown() }

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