package art

fun List<Element>.toMarkdown(): String = buildString {
    for (element in this@toMarkdown) {
        when (element) {
            is Heading -> appendLine("#".repeat(element.level) + " " + element.span.text)
            is Paragraph -> appendLine(element.spans.joinToString(" ") { it.text })
            is Bullets -> for (item in element.items) {
                appendLine("- ${item.text}")
            }

            is Sequence -> for ((index, item) in element.items.withIndex()) {
                appendLine("${index + 1}. ${item.text}")
            }

            else -> error("Unsupported element: $element")
        }
    }
}