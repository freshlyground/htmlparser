package fg.htmlparser

import fg.htmlparser.html.Node

class Path(elements: List<Element>, val absolute: Boolean) {

    constructor(vararg elements: Element, absolute: Boolean) :
            this(elements = elements.toList(), absolute = absolute)

    private val elements: List<Element> = elements.toList()

    val size: Int
        get() = elements.size

    fun firstElement(): Element {
        return elements.first()
    }

    fun withoutFirstElement(): Path {
        val withoutFirstElement = elements.subList(1, elements.size)
        return Path(withoutFirstElement, absolute = false)
    }

    fun append(element: Element): Path {

        val list = elements.toMutableList()
        list.add(element)
        return Path(list, absolute = absolute)
    }

    override fun toString(): String {
        val path = elements.map { it.toString() }.reduce({ a, b -> "$a/$b" })
        return if (absolute) "/$path" else path
    }

    class Element(val name: String, val index: Int = 0) {

        fun matches(node: Node): Boolean {

            if (node is fg.htmlparser.html.Element) {
                return node.name == name && node.index == index
            } else {
                return false
            }
        }

        override fun toString(): String {
            return "$name[$index]"
        }

        companion object {

            fun from(string: String): Element {

                if (string.endsWith("]")) {
                    val name = string.substringBefore("[")
                    val indexBracketStart = string.indexOf('[')
                    val index: Int = string.substring(indexBracketStart + 1, string.lastIndex).toInt()
                    return Element(name = name, index = index)
                } else {
                    return Element(name = string)
                }
            }
        }


    }

    companion object {

        fun from(string: String): Path {

            val splitted = string.split("/")
            val elements: MutableList<Element> = arrayListOf()
            splitted
                    .filter { it.isNotBlank() }
                    .mapTo(elements) { it.toElement() }

            return Path(elements, string.startsWith("/"))
        }
    }


}

fun String.toPath(): Path {
    return Path.from(this)
}

fun String.toElement(): Path.Element {
    return Path.Element.from(this)
}