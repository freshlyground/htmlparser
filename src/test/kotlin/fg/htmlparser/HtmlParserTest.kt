package fg.htmlparser

import fg.htmlparser.html.HtmlStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Ignore
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths

class HtmlParserTest {

    private val testClassDir: Path = Paths.get(HtmlParserTest::class.java.getResource(".").toURI())

    @Test
    fun parse_single_with_content() {
        val s = "<html>a</html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html"))
        assertEquals("a", html.selectNode("/html")?.content())
    }

    @Test
    fun parse_element_with_content() {
        val s = "<html><body>a</body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/body"))
        assertEquals("a", html.selectNode("/html/body")?.content())
    }

    @Test
    fun parse_unclosed_element() {
        val s = "<html><p>a</html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/p"))
        assertEquals("a", html.selectNode("/html")?.content())
    }

    @Test
    fun parse_open_element() {
        val s = "<html><body><p>something</p></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/body/p"))
        assertEquals("something", html.selectNode("/html/body/p")?.content())
    }

    @Test
    fun parse_two_open_elements_with_content() {
        val s = "<html><head>a</head><body>b</body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/head"))
        assertNotNull(html.selectNode("/html/body"))
        assertEquals("a", html.selectNode("/html/head")?.content())
        assertEquals("b", html.selectNode("/html/body")?.content())
    }

    @Test
    fun parse_empty_tag() {
        val s = "<html><body>line 1<br/>line 2</body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/body/br"))
        assertEquals("line 1line 2", html.selectNode("/html/body")?.content())
    }

    @Test
    fun html_selectContent() {
        val s = "<html><body><p>content</p></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("content", html.selectContent("/html/body/p"))
    }

    @Test
    fun html_selectContent_given_content_with_space_in_the_middle() {
        val s = "<html><body><p>content with space</p></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("content with space", html.selectContent("/html/body/p"))
    }

    @Test
    fun html_selectContent_given_content_with_space_around() {
        val s = "<html><body><p> content with space around </p></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals(" content with space around ", html.selectContent("/html/body/p"))
    }

    @Test
    fun html_selectContent_given_element_with_attribute() {
        val s = "<html><body>" +
                "<p style='background-color: red'>" +
                "content" +
                "</p>" +
                "</body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("content", html.selectContent("/html/body/p"))
    }

    @Test
    fun html_selectContent_given_element_with_two_attributes() {
        val s = "<html><body>" +
                "<p style='background-color: red' width='100%'>" +
                "content" +
                "</p>" +
                "</body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("content", html.selectContent("/html/body/p"))
    }

    @Test
    fun parse_handles_() {
        val s = "<html><body><img/><p>content</p></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("content", html.selectContent("/html/body/p"))
    }

    @Test
    fun html_selectContent_given_empty_element() {
        val s = "<html><body><p/></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("", html.selectContent("/html/body/p"))
    }

    @Test
    fun parse_unclosed_tag() {
        val s = "<html>" +
                "<head>" +
                "<body></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/head"))
        assertNotNull(html.selectNode("/html/body"))
    }

    @Test
    fun parse_unclosed_tag2() {
        val s = "<html>" +
                "<head>" +
                "<meta>" +
                "</head>" +
                "<body>content</body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertNotNull(html.selectNode("/html/head"))
        assertNotNull(html.selectNode("/html/head/meta"))
        assertNotNull(html.selectNode("/html/body"))
        assertEquals("content", html.selectContent("/html/body/"))
    }

    @Test
    fun xxx() {
        val s = "<html>" +
                "<head>" +
                "<meta http-equiv=\"Content-Language\">" +
                "<meta http-equiv=\"Content-Type\">" +
                "<meta name=\"GENERATOR\">" +
                "<title>Some Title</title>" +
                "</head>" +
                "<body></body></html>"

        val html = HtmlParser(HtmlStream.parse(s)).parse()
        assertEquals("Some Title", html.selectContent("/html/head/title"))
    }

    @Test
    @Ignore
    fun asdfsdf() {

        val htmlString: String = testClassDir.resolve("blueberry.html").toFile().readText()
        val html = HtmlParser(HtmlStream.parse(htmlString)).parse()

        println(html.toXmlString())
    }
}