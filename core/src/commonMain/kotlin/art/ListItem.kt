package art

import kotlinx.serialization.Serializable

@Serializable
class ListItem(
    val elements: List<Element> = emptyList()
)