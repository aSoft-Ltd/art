package art

class Heading(
    val level: Int,
    override val indent: Int,
    val span: Span
) : Element