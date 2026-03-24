package art

class Paragraph(
    override val indent: Int,
    val spans: List<Span>
) : Element