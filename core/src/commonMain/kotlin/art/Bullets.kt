package art

import kotlinx.serialization.Serializable

@Serializable
class Bullets(
    override val indent: Int,
    val items: List<ListItem>
) : Element