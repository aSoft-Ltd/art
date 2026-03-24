package art

import kotlinx.serialization.Serializable

@Serializable
class Paragraph(
    override val indent: Int,
    val spans: List<Span>
) : Element