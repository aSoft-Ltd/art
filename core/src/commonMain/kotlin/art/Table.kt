package art

import kotlinx.serialization.Serializable

@Serializable
class Table(
    override val indent: Int,
    val columns: List<Column> = listOf(),
    val rows: List<Row> = listOf()
) : Element