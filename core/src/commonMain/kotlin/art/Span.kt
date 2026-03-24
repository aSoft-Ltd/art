package art

import kotlinx.serialization.Serializable

@Serializable
data class Span(
    val text: String,
    override val indent: Int = 0,
    val link: String? = null,
    val style: Style? = null,
) : Element