package art

class ListBuilder(
    val items: MutableList<Span> = mutableListOf()
) {
    fun li(text: String) {
        items.add(Span(text))
    }
}

fun DocumentBuilder.ul(builder: ListBuilder.() -> Unit) {
    val bullets = ListBuilder()
    bullets.builder()
    elements.add(Bullets(0, bullets.items))
}

fun DocumentBuilder.ol(builder: ListBuilder.() -> Unit) {
    val bullets = ListBuilder()
    bullets.builder()
    elements.add(Sequence(0, bullets.items))
}