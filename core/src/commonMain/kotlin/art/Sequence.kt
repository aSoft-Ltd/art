package art

class Sequence(
    override val indent: Int,
    val items: List<List<Span>>
) : Element