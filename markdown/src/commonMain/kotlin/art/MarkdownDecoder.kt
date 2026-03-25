package art

fun String.parseMarkdown(): List<Element> {
    val elements = mutableListOf<Element>()
    val lines = this.lines()
    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        if (line.isBlank()) {
            i++
            continue
        }

        when {
            line.startsWith("#") -> {
                val level = line.takeWhile { it == '#' }.length
                val text = line.drop(level).trim()
                elements.add(Heading(level, 0, Span(text)))
                i++
            }

            line.startsWith("- ") -> {
                val items = mutableListOf<List<Span>>()
                while (i < lines.size && lines[i].startsWith("- ")) {
                    items.add(lines[i].substring(2).parseSpans())
                    i++
                }
                elements.add(Bullets(0, items))
            }

            line.firstOrNull()?.isDigit() == true && line.contains(". ") -> {
                val items = mutableListOf<List<Span>>()
                while (i < lines.size && lines[i].firstOrNull()?.isDigit() == true && lines[i].contains(". ")) {
                    val dotIndex = lines[i].indexOf(". ")
                    items.add(lines[i].substring(dotIndex + 2).parseSpans())
                    i++
                }
                elements.add(Sequence(0, items))
            }

            line.startsWith("|") -> {
                val headerLine = line
                i++
                if (i < lines.size && lines[i].startsWith("|") && lines[i].contains("---")) {
                    i++ // skip separator line
                    val headerCells = headerLine.split("|").filter { it.isNotBlank() }.map { it.trim() }
                    val columns = headerCells.map { Column(spans = it.parseSpans()) }

                    val rows = mutableListOf<Row>()
                    while (i < lines.size && lines[i].startsWith("|")) {
                        val rowCells = lines[i].split("|").filter { it.isNotBlank() }.map { it.trim().parseSpans() }
                        val row = Row()
                        row.cells.addAll(rowCells)
                        rows.add(row)
                        i++
                    }
                    elements.add(Table(0, columns, rows))
                } else {
                    // Not a table, just a paragraph starting with |
                    elements.add(Paragraph(0, line.parseSpans()))
                }
            }

            else -> {
                elements.add(Paragraph(0, line.parseSpans()))
                i++
            }
        }
    }
    return elements
}

private fun String.parseSpans(): List<Span> {
    val spans = mutableListOf<Span>()
    var current = this
    val regex = Regex("""(\*\*.*?\*\*|\*.*?\*|`.*?`|~~.*?~~|\[.*?\]\(.*?\))""")

    var lastIdx = 0
    regex.findAll(this).forEach { match ->
        if (match.range.first > lastIdx) {
            spans.add(Span(this.substring(lastIdx, match.range.first)))
        }
        val text = match.value
        when {
            text.startsWith("**") && text.endsWith("**") ->
                spans.add(Span(text.removeSurrounding("**"), style = TextStyle(bold = true)))

            text.startsWith("*") && text.endsWith("*") ->
                spans.add(Span(text.removeSurrounding("*"), style = TextStyle(italic = true)))

            text.startsWith("`") && text.endsWith("`") ->
                spans.add(Span(text.removeSurrounding("`"), style = TextStyle(quote = true)))

            text.startsWith("~~") && text.endsWith("~~") -> {
                val content = text.removeSurrounding("~~")
                if (content.startsWith("[") && content.contains("](")) {
                    val linkRegex = Regex("""\[(.*?)]\((.*?)\)""")
                    val linkMatch = linkRegex.find(content)
                    if (linkMatch != null) {
                        spans.add(Span(linkMatch.groupValues[1], link = linkMatch.groupValues[2], style = TextStyle(strike = true)))
                    } else {
                        spans.add(Span(content, style = TextStyle(strike = true)))
                    }
                } else {
                    spans.add(Span(content, style = TextStyle(strike = true)))
                }
            }

            text.startsWith("[") && text.contains("](") -> {
                val linkRegex = Regex("""\[(.*?)]\((.*?)\)""")
                val linkMatch = linkRegex.find(text)
                if (linkMatch != null) {
                    spans.add(Span(linkMatch.groupValues[1], link = linkMatch.groupValues[2]))
                }
            }
        }
        lastIdx = match.range.last + 1
    }
    if (lastIdx < length) {
        spans.add(Span(substring(lastIdx)))
    }
    return if (spans.isEmpty()) listOf(Span(this)) else spans
}