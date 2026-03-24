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

            is Table -> appendLine(element.toMarkdown())

            else -> error("Unsupported element: $element")
        }
    }
}

private fun Table.toMarkdown(): String = buildString {
    if (rows.size <= 1) return@buildString
    val header = rows[0]
    appendLine(header.toMarkdown())
    appendLine(header.cells.toMarkdownRow { "---" })
    for (row in rows.drop(1)) appendLine(row.toMarkdown())
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