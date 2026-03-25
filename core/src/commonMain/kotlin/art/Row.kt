package art

import kotlinx.serialization.Serializable

@Serializable
class Row(
    val cells: MutableList<List<Span>> = mutableListOf()
)