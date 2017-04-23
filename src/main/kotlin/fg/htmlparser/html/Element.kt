package fg.htmlparser.html

import fg.htmlparser.Path
import fg.htmlparser.toPath

open class Element(val name: String,
                   val attributes: List<Attribute>,
                   val closingSlash: Boolean = false,
                   lineNumber: Int = 1) : Node(lineNumber) {

    val path: Path
        get() {
            if (parent != null) {
                return parent!!.path.append(Path.Element(name, index))
            } else {
                return Path(listOf(Path.Element(name, index)), true)
            }
        }

    val children: MutableList<Node> = arrayListOf()

    fun moveChildrenToParent() {

        if (children.isEmpty()) {
            return
        }

        while (children.isNotEmpty()) {
            val child = children.removeAt(children.lastIndex)
            parent?.add(child)
        }
    }

    fun add(node: Node) {
        node.parent = this
        node.index = countSiblings(node)
        children.add(node)
    }

    fun countSiblings(node: Node): Int {

        val count = children.count {
            node is Element
                    && it is Element
                    && it.name.toLowerCase() == node.name.toLowerCase()
        }
        return count
    }

    fun selectContent(path: String): String? {
        return selectContent(path.toPath())
    }

    fun selectContent(path: Path): String? {
        val node = selectNode(path)
        if (node == null) {
            return null
        } else if (node is Text) {
            return node.value
        } else if (node is Element) {
            return node.content()
        } else {
            throw UnsupportedOperationException()
        }
    }

    fun selectNode(path: String): Node? {
        return selectNode(path.toPath())
    }

    open fun selectNode(path: Path): Node? {

        if (path.absolute) {
            if (!path.firstElement().matches(this)) {
                return null
            } else if (path.size > 1) {
                return selectNode(path.withoutFirstElement())
            } else {
                return if (path.firstElement().matches(this)) this else null
            }
        }

        val element = path.firstElement()
        val match = children.find { element.matches(it) }
        if (path.size > 1 && match != null && match is Element) {
            return match.selectNode(path.withoutFirstElement())
        } else {
            return match
        }
    }

    override fun content(): String {
        val s = StringBuilder()
        for (child in children) {
            s.append(child.content())
        }
        return s.toString()
    }

    override fun toString(): String {
        return "[$lineNumber]: $path"
    }

    override fun toXmlString(): String {
        val s = StringBuilder()
        s.append("<$name>")
        children.forEach {
            s.append("\n").append(it.toXmlString())
        }
        s.append("</$name>")
        return s.toString()
    }
}