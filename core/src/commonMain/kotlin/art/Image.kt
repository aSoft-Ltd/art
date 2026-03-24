package art

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val text: String,
    override val indent: Int = 0,
    val link: String? = null,
    val style: TextStyle? = null,
) : Element