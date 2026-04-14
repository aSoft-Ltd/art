package art.html

import art.Bullets
import art.Art
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

internal fun FlowContent.bullets(
    b: Bullets,
    padding: String
) {
    div {
        for (item in b.items) div {
            style = "display: flex"
            div { +"•" }
            Art(
                content = item.elements,
                style = "padding-left: $padding"
            )
        }
    }
}