package fg.htmlparser.html

import org.junit.Assert.assertEquals
import org.junit.Test

class HtmlStreamTest {

    @Test
    fun forward() {

        val items = listOf(
                ElementStart("html"),
                ElementStart("body"),
                ElementStart("p"),
                ContentItem("content"),
                ElementEnd("p"),
                ElementEnd("body"),
                ElementEnd("html"))
        val stream = HtmlStream(items)

        assertEquals("<html>", stream.forward().toString())
        assertEquals("<body>", stream.forward().toString())
        assertEquals("<p>", stream.forward().toString())
        assertEquals("content", stream.forward().toString())
        assertEquals("</p>", stream.forward().toString())
        assertEquals("</body>", stream.forward().toString())
        assertEquals("</html>", stream.forward().toString())
    }

    @Test
    fun atEnd() {

        val items = listOf(
                ElementStart("html"),
                ElementStart("body"),
                ElementEnd("body"),
                ElementEnd("html"))
        val stream = HtmlStream(items)

        assertEquals(false, stream.atEnd())
        assertEquals("<html>", stream.forward().toString())
        assertEquals(false, stream.atEnd())
        assertEquals("<body>", stream.forward().toString())
        assertEquals(false, stream.atEnd())
        assertEquals("</body>", stream.forward().toString())
        assertEquals(false, stream.atEnd())
        assertEquals("</html>", stream.forward().toString())
        assertEquals(true, stream.atEnd())
    }

    @Test
    fun forwardUntil() {

        val items = listOf(
                ElementStart("html"),
                ElementStart("body"),
                ElementStart("p"),
                ContentItem("content"),
                ElementEnd("p"),
                ElementEnd("body"),
                ElementEnd("html")
        )
        val stream = HtmlStream(items)
        val previous = stream.forwardUntil { it is ElementStart && it.name == "p" }

        assertEquals("<html>", previous[0].toString())
        assertEquals("<body>", previous[1].toString())

        assertEquals("<p>", stream.current.toString())
    }

    @Test
    fun forwardUntilElementStart() {

        val items = listOf(
                ElementStart("html"),
                ElementStart("body"),
                ElementStart("p"),
                ContentItem("content"),
                ElementEnd("p"),
                ElementEnd("body"),
                ElementEnd("html")
        )
        val stream = HtmlStream(items)
        val previous = stream.forwardUntilElementStart { it.name == "p" }

        assertEquals("<html>", previous[0].toString())
        assertEquals("<body>", previous[1].toString())

        assertEquals("<p>", stream.current.toString())
    }

    @Test
    fun forwardUntilElementEnd() {

        val items = listOf(
                ElementStart("html"),
                ElementStart("body"),
                ElementStart("p"),
                ContentItem("content"),
                ElementEnd("p"),
                ElementEnd("body"),
                ElementEnd("html")
        )
        val stream = HtmlStream(items)
        val previous = stream.forwardUntilElementEnd { it.name == "p" }

        assertEquals("<html>", previous[0].toString())
        assertEquals("<body>", previous[1].toString())
        assertEquals("<p>", previous[2].toString())
        assertEquals("content", previous[3].toString())

        assertEquals("</p>", stream.current.toString())
    }
}