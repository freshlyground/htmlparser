package fg.htmlparser.html

import org.junit.Assert.assertEquals
import org.junit.Test

class HtmlStreamParserTest {


    @Test
    fun parse_single_with_content() {

        val s = "<html>a</html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_open_element() {

        val s = "<html><body><p>something</p></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_two_open_elements_with_content() {

        val s = "<html><head>a</head><body>b</html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_empty_tag() {

        val s = "<html><body>line 1<br/>line 2</body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_content_with_space_in_the_middle() {

        val s = "<html><body><p>content with space</p></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_content_with_space_around() {

        val s = "<html><body><p> content with space around </p></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_content_with_attribute() {

        val s = "<html><body style='background: red'></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_content_with_two_attributes() {

        val s = "<html><body style='background: red' width='100%'></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_attribute_with_value_in_double_quotes() {
        val s = "<html><head><body width=\"100%\"></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_attribute_far_from_element_start() {
        val s = "<html><head><body     width='100%'></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals("<html><head><body width='100%'></body></html>",
                stream.toString())
    }

    @Test
    fun parse_attribute_on_next_line_from_element_start() {
        val s = "<html><head><body\n     width='100%'></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals("<html><head><body\n width='100%'></body></html>",
                stream.toString())
    }

    @Test
    fun parse_closed_element_with_attributes() {
        val s = "<html><body><img href='http' target='_blank'/></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }

    @Test
    fun parse_unclosed_element() {
        val s = "<html><head><body></body></html>"

        val stream = HtmlStreamParser(s).parse()
        assertEquals(s, stream.toString())
    }


}