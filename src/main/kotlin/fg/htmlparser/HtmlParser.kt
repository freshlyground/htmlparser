package fg.htmlparser

import fg.htmlparser.html.*
import kotlin.properties.Delegates

class HtmlParser(val stream: HtmlStream) {

    private var rootElement: Element by Delegates.notNull()
    private val parentElements: MutableList<Element> = arrayListOf()
    private val currContext: Element
        get() = parentElements.last()

    fun parse(): Element {

        stream.forwardUntilElementStart()
        rootElement = parseElement()
        parseContent(rootElement)

        if (parentElements.isNotEmpty()) {
            throw IllegalStateException("elementLevel expected to be empty: " + parentElements)
        }
        return rootElement
    }

    private fun parseElement(): Element {
        val elementStart = stream.currentElementStart
        val element = Element(
                name = elementStart.name,
                attributes = elementStart.attributes,
                closingSlash = elementStart.closed,
                lineNumber = elementStart.lineNumber)
        if (!elementStart.closed) {
            parentElements.add(element)
        }
        if (element.name.isBlank()) {
            println("element name is blank")
        }
        stream.forward()
        return element
    }

    private fun parseContent(context: Element) {
        if (stream.atEnd()) {
            return
        }

        val contents = stream.forwardUntil { it is ElementStart || it is ElementEnd }
        for (content in contents) {
            if (content is ContentItem) {
                context.add(Text(content.content))
            }
        }
        if (stream.current is ElementStart) {
            val element = parseElement()
            context.add(element)
            println("$element")
            if (element.closingSlash) {
                parseContent(context)
            } else {
                parseContent(element)
                if (!stream.atEnd()) {
                    parseContent(currContext)
                }
            }

        } else {

            handleElementEnd(stream.currentElementEnd, currContext)

            stream.forward()
        }
    }

    private fun handleElementEnd(elementEnd: ElementEnd, context: Element) {
        if (context.name == elementEnd.name) {
            if (context.closingSlash) {
                throw IllegalStateException("Parsed closing element for a already closed element")
            }
            parentElements.removeAt(parentElements.lastIndex)
        } else {
            val node = rootElement.selectNode(context.path)
            if (node != null) {
                if (node is Element) {
                    node.moveChildrenToParent()
                    parentElements.removeAt(parentElements.lastIndex)
                    handleElementEnd(elementEnd, currContext)
                }
            }
        }
    }

}