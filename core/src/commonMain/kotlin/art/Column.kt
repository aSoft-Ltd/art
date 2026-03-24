package art

import kotlinx.serialization.Serializable

@Serializable
class Column(
    val align: Align = Align.Left,
    val spans: List<Span> = listOf()
)