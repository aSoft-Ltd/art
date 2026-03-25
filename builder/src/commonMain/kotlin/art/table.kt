package art

class TableBuilder(
    val columns: MutableList<Column> = mutableListOf(),
    val rows: MutableList<Row> = mutableListOf()
) {
    fun columns(builder: ColumnBuilder.() -> Unit) {
        columns.addAll(ColumnBuilder().apply(builder).columns)
    }

    fun row(builder: RowBuilder.() -> Unit) {
        rows.add(Row(RowBuilder().apply(builder).cells))
    }

    internal fun build() = Table(0, columns, rows)
}

class ColumnBuilder(
    val columns: MutableList<Column> = mutableListOf()
) {
    fun column(text: String, align: Align = Align.Left) {
        columns.add(Column(align, listOf(Span(text))))
    }

    fun column(align: Align = Align.Left, builder: SpanBuilder.() -> Unit) {
        columns.add(Column(align, SpanBuilder().apply(builder).spans))
    }
}

class RowBuilder(
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
    elements.add(TableBuilder().apply(builder).build())
}