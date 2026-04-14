package art.tooling

fun String.slug() = toList().joinToString(separator = "") {
    when (it) {
        in listOf(' ') -> "_"
        in listOf('/') -> "_or_"
        in listOf('&') -> "_and_"
        in listOf('?', ',') -> ""
        else -> it.lowercase()
    }
}.replace("__", "_")