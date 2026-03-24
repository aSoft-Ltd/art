package art

import kotlinx.serialization.Serializable

@Serializable
class Table(
    override val indent: Int,
    val rows: List<Row> = listOf()
) : Element