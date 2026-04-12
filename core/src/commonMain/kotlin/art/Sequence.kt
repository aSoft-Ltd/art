package art

import kotlinx.serialization.Serializable

@Serializable
class Sequence(
    override val indent: Int,
    val indexing: Indexing,
    val closer: String,
    val items: List<ListItem>
) : Element {
    enum class Indexing {
        Numeric, ALPHABETIC, alphabetic,
    }
}