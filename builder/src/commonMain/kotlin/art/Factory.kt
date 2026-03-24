package art

fun document(builder: DocumentBuilder.() -> Unit): List<Element> {
    val doc = DocumentBuilder()
    doc.builder()
    return doc.elements
}