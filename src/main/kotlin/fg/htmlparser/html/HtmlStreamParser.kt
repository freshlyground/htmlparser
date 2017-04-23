package fg.htmlparser.html

import fg.htmlparser.html.Chars.WHITE_SPACE

class HtmlStreamParser(html: String) {

    private val reel: CharReel = CharReel(html)

    private val items: MutableList<StreamItem> = arrayListOf()

    fun parse(): HtmlStream {

        parseContent()

        return HtmlStream(items)
    }

    private fun parseElementStart() {

        val elementName = reel.forwardUntil(elementNameEnd)
        var closed = reel.current == '/' && reel.next == '>'
        val attributes: MutableList<Attribute> = arrayListOf()

        if (closed) {
            reel.forward()
        } else if (reel.current == ' ') {

            attributes.add(parseAttribute())

            do {
                reel.forwardUntil('>', '/', ' ')
                if (reel.current == '>') {
                    break
                } else if (reel.current == ' ') {
                    attributes.add(parseAttribute())
                } else {
                    break
                }
            } while (true)
        }
        if (reel.current == '/' && reel.next == '>') {
            closed = true
            reel.forward()
        }

        val elementStart = ElementStart(elementName, attributes, closed, reel.lineCount)
        items.add(elementStart)
    }

    private fun parseAttribute(): Attribute {

        while (reel.nextMatches(WHITE_SPACE)) {
            reel.forward()
        }

        val attributeName = reel.forwardUntil(' ', '=', '>')
        if (reel.current == '=') {
            if (reel.next == Chars.SINGLE_QUOTE) {
                reel.forward()
                val attributeValue = reel.forwardUntil(Chars.SINGLE_QUOTE)
                return Attribute(attributeName, attributeValue, Chars.SINGLE_QUOTE)
            } else if (reel.next == Chars.DOUBLE_QUOTE) {
                reel.forward()
                val attributeValue = reel.forwardUntil(Chars.DOUBLE_QUOTE)
                return Attribute(attributeName, attributeValue, Chars.DOUBLE_QUOTE)
            } else {
                val attributeValue = reel.forwardUntil(' ', '>')
                return Attribute(attributeName, attributeValue)
            }
        } else if (reel.current == ' ') {
            return Attribute(attributeName)
        } else {
            return Attribute(attributeName)
        }
    }

    private fun parseContent() {

        if (reel.atEnd()) {
            return
        }
        val content = reel.forwardUntil('<')
        if (reel.next == null) {
            //return context
            return
        } else if (content.isNotEmpty()) {
            items.add(ContentItem(content, reel.lineCount))
        }
        if (reel.next == '/') { // ElementEnd
            reel.forward()
            val endElementName = reel.forwardUntil('>')
            items.add(ElementEnd(endElementName, reel.lineCount))
        } else {
            parseElementStart()
        }
        parseContent()
    }

    companion object {

        private val elementNameEnd = listOf('>', '/', ' ')

    }
}