package art

import kotlinx.serialization.Serializable

@Serializable
class TextStyle(
    val bold: Boolean? = null,
    val italic: Boolean? = null,
    val quote: Boolean? = null,
    val strike: Boolean? = null,
    val underline: Boolean? = null,
    val color: String? = null,
)