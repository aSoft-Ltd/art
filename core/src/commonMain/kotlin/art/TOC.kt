package art

import kotlinx.serialization.Serializable

@Serializable
data class TOCItem(
    val level: Int,
    val prefix: String,
    val text: String,
    val salt: Int?,
    val indent: Int
)

/**
 * Generates a table of contents (TOC) for the list of elements.
 * It ignores all non-heading entries and focuses on the heading ones.
 * The heading entries are scoped as they are discovered (e.g., 3.1 for an H2 under the third H1).
 * Returns a structured list of [TOCItem]s which include level, prefix, text and indentation depth.
 */
fun List<Element>.toc(): List<TOCItem> {
    val counters = IntArray(7) { 0 }
    return filterIsInstance<Heading>().map { heading ->
        val level = heading.level.coerceIn(1, 6)
        
        // Reset all deeper levels
        for (i in level + 1..6) {
            counters[i] = 0
        }
        
        // Ensure parent levels are initialized to 1 if they haven't been encountered
        for (i in 1 until level) {
            if (counters[i] == 0) counters[i] = 1
        }
        
        // Increment current level
        counters[level]++
        
        val prefix = (1..level).joinToString(".") { counters[it].toString() }
        TOCItem(
            level = level,
            prefix = prefix,
            text = heading.span.text,
            salt = heading.salt,
            indent = level - 1
        )
    }
}