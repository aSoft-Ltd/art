package art

class TableBuilder(
    val rows: MutableList<Row> = mutableListOf()
) {
    fun header(builder: CellBuilder.() -> Unit) {
        rows.add(Row(header = true, CellBuilder().apply(builder).cells))
    }

    fun row(builder: CellBuilder.() -> Unit) {
        rows.add(Row(header = null, CellBuilder().apply(builder).cells))
    }
}

class CellBuilder(
    val cells: MutableList<List<Span>> = mutableListOf()
) {
    fun cell(text: String) {
        cells.add(listOf(Span(text)))
    }

    fun cell(builder: SpanBuilder.() -> Unit) {
        cells.add(SpanBuilder().apply(builder).spans)
    }
}

fun DocumentBuilder.table(builder: TableBuilder.() -> Unit) {
    elements.add(Table(0, TableBuilder().apply(builder).rows))
}