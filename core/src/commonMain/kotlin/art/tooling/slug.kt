package art.tooling

fun String.slug() = toList().joinToString(separator = "") {
    when (it) {
        in listOf(' ') -> "-"
        in listOf('/') -> "-or-"
        in listOf('&') -> "-and-"
        in listOf('?', ',') -> ""
        else -> it.lowercase()
    }
}.replace("--", "-")