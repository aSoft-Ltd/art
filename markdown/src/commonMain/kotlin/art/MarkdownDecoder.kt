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

        val indent = line.takeWhile { it == ' ' }.length
        val trimmedLine = line.trimStart()

        when {
            trimmedLine.startsWith("#") -> {
                val level = trimmedLine.takeWhile { it == '#' }.length
                val text = trimmedLine.drop(level).trim()
                elements.add(Heading(level, indent, Span(text), elements.filterIsInstance<Heading>().size + 1))
                i++
            }

            trimmedLine.startsWith("- ") -> {
                val items = mutableListOf<ListItem>()
                val listIndent = indent
                while (i < lines.size) {
                    val l = lines[i]
                    if (l.isBlank()) {
                        i++
                        continue
                    }
                    val currIndent = l.takeWhile { it == ' ' }.length
                    val currTrimmed = l.trimStart()

                    if (currIndent == listIndent && currTrimmed.startsWith("- ")) {
                        val text = currTrimmed.substring(2)
                        i++
                        val nestedLines = mutableListOf<String>()
                        while (i < lines.size) {
                            val nl = lines[i]
                            if (nl.isBlank()) {
                                var j = i + 1
                                while (j < lines.size && lines[j].isBlank()) j++
                                if (j < lines.size && lines[j].takeWhile { it == ' ' }.length > listIndent) {
                                    nestedLines.add(nl)
                                    i++
                                } else break
                            } else if (nl.takeWhile { it == ' ' }.length > listIndent) {
                                nestedLines.add(nl)
                                i++
                            } else break
                        }
                        val nestedElements = if (nestedLines.isNotEmpty()) {
                            nestedLines.joinToString("\n").parseMarkdown()
                        } else emptyList()
                        items.add(ListItem(text.parseSpans() + nestedElements))
                    } else break
                }
                elements.add(Bullets(listIndent, items))
            }

            (trimmedLine.firstOrNull()?.isDigit() == true || (trimmedLine.length > 2 && trimmedLine[0].isLetter() && trimmedLine[1] == '.' && trimmedLine[2] == ' ')) && trimmedLine.contains(". ") -> {
                val items = mutableListOf<ListItem>()
                val listIndent = indent
                val firstDotIndex = trimmedLine.indexOf(". ")
                val indexing = when {
                    trimmedLine[0].isDigit() -> Sequence.Indexing.Numeric
                    trimmedLine[0].isUpperCase() -> Sequence.Indexing.ALPHABETIC
                    else -> Sequence.Indexing.alphabetic
                }
                val closer = trimmedLine.substring(firstDotIndex, firstDotIndex + 1)

                while (i < lines.size) {
                    val l = lines[i]
                    if (l.isBlank()) {
                        i++
                        continue
                    }
                    val currIndent = l.takeWhile { it == ' ' }.length
                    val currTrimmed = l.trimStart()

                    if (currIndent == listIndent && (currTrimmed.firstOrNull()
                            ?.isDigit() == true || (currTrimmed.length > 2 && currTrimmed[0].isLetter() && currTrimmed[1] == '.' && currTrimmed[2] == ' ')) && currTrimmed.contains(". ")
                    ) {
                        val dIdx = currTrimmed.indexOf(". ")
                        val text = currTrimmed.substring(dIdx + 2)
                        i++
                        val nestedLines = mutableListOf<String>()
                        while (i < lines.size) {
                            val nl = lines[i]
                            if (nl.isBlank()) {
                                var j = i + 1
                                while (j < lines.size && lines[j].isBlank()) j++
                                if (j < lines.size && lines[j].takeWhile { it == ' ' }.length > listIndent) {
                                    nestedLines.add(nl)
                                    i++
                                } else break
                            } else if (nl.takeWhile { it == ' ' }.length > listIndent) {
                                nestedLines.add(nl)
                                i++
                            } else break
                        }
                        val nestedElements = if (nestedLines.isNotEmpty()) {
                            nestedLines.joinToString("\n").parseMarkdown()
                        } else emptyList()
                        items.add(ListItem(text.parseSpans() + nestedElements))
                    } else break
                }
                elements.add(Sequence(listIndent, indexing, closer, items))
            }

            trimmedLine.startsWith("|") -> {
                val headerLine = trimmedLine
                i++
                if (i < lines.size && lines[i].trimStart().startsWith("|") && lines[i].contains("---")) {
                    i++ // skip separator line
                    val headerCells = headerLine.split("|").filter { it.isNotBlank() }.map { it.trim() }
                    val columns = headerCells.map { Column(spans = it.parseSpans()) }

                    val rows = mutableListOf<Row>()
                    while (i < lines.size && lines[i].trimStart().startsWith("|")) {
                        val rowCells = lines[i].trimStart().split("|").filter { it.isNotBlank() }.map { it.trim().parseSpans() }
                        val row = Row()
                        row.cells.addAll(rowCells)
                        rows.add(row)
                        i++
                    }
                    elements.add(Table(indent, columns, rows))
                } else {
                    elements.add(Paragraph(indent, trimmedLine.parseSpans()))
                    i++
                }
            }

            else -> {
                elements.add(Paragraph(indent, trimmedLine.parseSpans()))
                i++
            }
        }
    }
    return elements
}

private fun String.parseSpans(): List<Span> {
    val spans = mutableListOf<Span>()
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