package art

fun DocumentBuilder.p(text: String) {
    elements.add(Paragraph(0, listOf(Span(text))))
}