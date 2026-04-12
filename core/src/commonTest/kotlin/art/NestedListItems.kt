package art

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class NestedListItems {

    @Test
    fun should_be_able_to_create_a_nested_list() {
        val doc = document {
            ol {
                li {
                    text("Welcome to the first item")
                }

                li {
                    text("This is another item")
                }
                
                li { 
                    text("Since this works")
                    ol { 
                        li("One")
                        li("Two")
                        li("Three")
                    }
                }
            }
        }
        val json = Json { prettyPrint = true }
        val encoded = json.encodeToString(doc)
        println(encoded)
        
        val decoded = json.decodeFromString<List<Element>>(encoded)
        val list = decoded[0] as Sequence
        val thirdItem = list.items[2]
        val nestedList = thirdItem.elements[0] as Sequence
        assertEquals(3, nestedList.items.size)
    }
}