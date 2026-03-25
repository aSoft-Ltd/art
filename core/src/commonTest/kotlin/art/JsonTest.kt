package art

import kotlinx.serialization.json.Json
import kotlin.test.Test

class JsonTest {

    @Test
    fun should_be_able_to_create_a_json_object() {
        val doc = document {
            ol {
                li {
                    text("Welcome to ")
                    link("Google", "https://www.google.com")
                }
                li {
                    text("This text is ")
                    bold("bold")
                }

                li {
                    text("This text is ")
                    italic("italic")
                }

                li {
                    text("This text is ")
                    quoted("quoted")
                }

                li {
                    text("This text is ")
                    struck("struck through")
                }
            }
        }

        val json = Json { prettyPrint = true }
        println(json.encodeToString(doc))
    }
}