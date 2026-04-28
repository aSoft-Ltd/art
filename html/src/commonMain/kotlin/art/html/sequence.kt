package art.html

import art.Art
import art.Sequence
import art.Sequence.Indexing.ALPHABETIC
import art.Sequence.Indexing.Numeric
import art.Sequence.Indexing.alphabetic
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

internal fun FlowContent.sequence(
    s: Sequence,
    padding: String
) {
    div {
        for ((index, item) in s.items.withIndex()) {
            val entry = when (s.indexing) {
                Numeric -> 1 + index
                ALPHABETIC -> 'A' + index
                alphabetic -> 'a' + index
            }
            div {
                style = "display: flex; padding-top: 0.5rem"
                div { +"$entry${s.closer}" }

                Art(
                    content = item.elements,
                    style = "padding-left: $padding"
                )
            }
        }
    }
}