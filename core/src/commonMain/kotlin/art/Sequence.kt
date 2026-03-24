package art

import kotlinx.serialization.Serializable

@Serializable
class Sequence(
    override val indent: Int,
    val items: List<List<Span>>
) : Element