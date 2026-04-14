package art

class SpanBuilder(
    val spans: MutableList<Span> = mutableListOf()
) {
    fun text(text: String) {
        spans.add(Span(text))
    }

    fun bold(text: String) {
        spans.add(Span(text, style = TextStyle(bold = true)))
    }

    fun italic(text: String) {
        spans.add(Span(text, style = TextStyle(italic = true)))
    }

    fun quoted(text: String) {
        spans.add(Span(text, style = TextStyle(quote = true)))
    }

    fun struck(text: String) {
        spans.add(Span(text, style = TextStyle(strike = true)))
    }

    fun link(text: String, link: String) {
        spans.add(Span(text, link = link))
    }
}


fun DocumentBuilder.p(text: String) {
    elements.add(Paragraph(0, listOf(Span(text))))
}

fun DocumentBuilder.p(builder: SpanBuilder.() -> Unit) {
    val spans = SpanBuilder()
    spans.builder()
    elements.add(Paragraph(0, spans.spans))
}