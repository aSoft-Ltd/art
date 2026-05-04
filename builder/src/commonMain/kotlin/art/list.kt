package art

class ListItemBuilder(
    val elements: MutableList<Element> = mutableListOf()
) {
    fun text(text: String) {
        elements.add(Span(text))
    }

    fun bold(text: String) {
        elements.add(Span(text, style = TextStyle(bold = true)))
    }

    fun italic(text: String) {
        elements.add(Span(text, style = TextStyle(italic = true)))
    }

    fun quoted(text: String) {
        elements.add(Span(text, style = TextStyle(quote = true)))
    }

    fun struck(text: String) {
        elements.add(Span(text, style = TextStyle(strike = true)))
    }

    fun link(text: String, link: String) {
        elements.add(Span(text, link = link))
    }

    fun p(text: String) {
        elements.add(Span(text))
    }

    fun p(builder: SpanBuilder.() -> Unit) {
        val spans = SpanBuilder()
        spans.builder()
        elements.add(Paragraph(1, spans.spans))
    }

    fun ul(builder: ListBuilder.() -> Unit) {
        val bullets = ListBuilder()
        bullets.builder()
        elements.add(Bullets(0, bullets.items))
    }

    fun ol(
        indexing: Sequence.Indexing = Sequence.Indexing.Numeric,
        closer: String = ".",
        builder: ListBuilder.() -> Unit
    ) {
        val bullets = ListBuilder()
        bullets.builder()
        elements.add(Sequence(0, indexing, closer, bullets.items))
    }
}

class ListBuilder(
    val items: MutableList<ListItem> = mutableListOf()
) {
    fun li(text: String) {
        items.add(ListItem(listOf(Span(text))))
    }

    fun li(builder: ListItemBuilder.() -> Unit) {
        val b = ListItemBuilder()
        b.builder()
        items.add(ListItem(b.elements))
    }
}

fun DocumentBuilder.ul(builder: ListBuilder.() -> Unit) {
    val bullets = ListBuilder()
    bullets.builder()
    elements.add(Bullets(0, bullets.items))
}

fun DocumentBuilder.ol(
    indexing: Sequence.Indexing = Sequence.Indexing.Numeric,
    closer: String = ".",
    builder: ListBuilder.() -> Unit
) {
    val bullets = ListBuilder()
    bullets.builder()
    elements.add(Sequence(0, indexing, closer, bullets.items))
}