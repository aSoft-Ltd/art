package art

import kotlinx.serialization.Serializable

@Serializable
class Row(
    val header: Boolean? = null,
    val cells: MutableList<List<Span>> = mutableListOf()
)