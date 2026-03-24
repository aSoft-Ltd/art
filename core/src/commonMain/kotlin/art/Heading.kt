package art

import kotlinx.serialization.Serializable

@Serializable
class Heading(
    val level: Int,
    override val indent: Int,
    val span: Span
) : Element