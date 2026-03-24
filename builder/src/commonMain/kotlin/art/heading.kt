package art

private fun DocumentBuilder.heading(level: Int, text: String) {
    elements.add(Heading(level, 0, Span(text)))
}

fun DocumentBuilder.h1(text: String) = heading(1, text)

fun DocumentBuilder.h2(text: String) = heading(2, text)

fun DocumentBuilder.h3(text: String) = heading(3, text)

fun DocumentBuilder.h4(text: String) = heading(4, text)

fun DocumentBuilder.h5(text: String) = heading(5, text)

fun DocumentBuilder.h6(text: String) = heading(6, text)