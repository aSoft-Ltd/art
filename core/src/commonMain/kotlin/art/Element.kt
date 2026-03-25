package art

import kotlinx.serialization.Serializable

@Serializable
sealed interface Element {
    val indent: Int
}