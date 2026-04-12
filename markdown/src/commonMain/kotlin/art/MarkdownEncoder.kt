package art

import kotlin.jvm.JvmName

fun List<Element>.toMarkdown(
    numbers: Boolean = false,
    indent: Int = 0,
    contents: List<TOCItem>? = null
): String = buildString {
    val toc = contents ?: if (indent == 0) toc() else emptyList()
    val tab = "    ".repeat(indent)
    var i = 0
    while (i < this@toMarkdown.size) {
        val element = this@toMarkdown[i]
        when (element) {
            is Span -> {
                val spans = mutableListOf<Span>()
                while (i < this@toMarkdown.size && this@toMarkdown[i] is Span) {
                    spans.add(this@toMarkdown[i] as Span)
                    i++
                }
                appendLine(tab + spans.toMarkdown())
                if (i < this@toMarkdown.size) appendLine()
            }

            is Heading -> {
                val prefix = if (numbers) toc.find { it.text == element.span.text }?.prefix?.let { "$it. " } ?: "" else ""
                appendLine(tab + "#".repeat(element.level) + " " + prefix + element.span.text + "\n")
                i++
            }

            is Paragraph -> {
                appendLine(tab + element.spans.toMarkdown())
                appendLine()
                i++
            }

            is Bullets -> {
                for (item in element.items) {
                    appendListItem("$tab- ", item, numbers, indent, toc)
                }
                if (indent == 0) appendLine()
                i++
            }

            is Sequence -> {
                for ((index, item) in element.items.withIndex()) {
                    val entry = when (element.indexing) {
                        Sequence.Indexing.Numeric -> 1 + index
                        Sequence.Indexing.ALPHABETIC -> 'A' + index
                        Sequence.Indexing.alphabetic -> 'a' + index
                    }
                    appendListItem("$tab$entry${element.closer} ", item, numbers, indent, toc)
                }
                if (indent == 0) appendLine()
                i++
            }

            is Table -> {
                appendLine(element.toMarkdown(tab))
                appendLine()
                i++
            }

            else -> i++
        }
    }
}.let { if (indent == 0) it.trimEnd() + "\n" else it }

private fun StringBuilder.appendListItem(
    prefix: String,
    item: ListItem,
    numbers: Boolean,
    indent: Int,
    toc: List<TOCItem>
) {
    appendLine()
    append(prefix)
    if (item.elements.isEmpty()) {
        appendLine()
        return
    }

    var idx = 0
    if (item.elements.isNotEmpty() && item.elements[idx] is Span) {
        val leadingSpans = mutableListOf<Span>()
        while (idx < item.elements.size && item.elements[idx] is Span) {
            leadingSpans.add(item.elements[idx] as Span)
            idx++
        }
        append(leadingSpans.toMarkdown())
        appendLine()
    } else {
        appendLine()
    }

    if (idx < item.elements.size) {
        val remaining = item.elements.drop(idx)
        append(remaining.toMarkdown(numbers, indent + 1, toc))
    }
    appendLine()
}

private fun Table.toMarkdown(tab: String): String = buildString {
    if (rows.isEmpty() || columns.isEmpty()) return@buildString
    appendLine(tab + columns.toMarkdownRow { it.spans.toMarkdown() })
    appendLine(tab + columns.toMarkdownRow { "---" })
    for (row in rows) appendLine(tab + row.toMarkdown())
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
