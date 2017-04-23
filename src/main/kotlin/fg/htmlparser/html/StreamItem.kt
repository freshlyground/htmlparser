package fg.htmlparser.html

abstract class StreamItem(val lineNumber: Int)

class ElementStart(name: String,
                   val attributes: List<Attribute> = listOf(),
                   val closed: Boolean = false,
                   lineNumber: Int = 1) : ElementItem(name, lineNumber) {

    override fun toString(): String {
        val s = StringBuilder()
        s.append("<")
        s.append(name)
        if (attributes.isNotEmpty()) {

            for (attr in attributes) {
                s.append(" ")
                s.append(attr)
            }
        }
        if (closed) {
            s.append("/")
        }
        s.append(">")
        return s.toString()
    }
}

class Attribute(val name: String, val value: String? = null, val valueEnclose: Char? = null) {

    override fun toString(): String {
        val s = StringBuilder()
        s.append(name)
        if (value != null) {
            s.append("=")
            if (valueEnclose != null) {
                s.append(valueEnclose)
            }
            s.append(value)
            if (valueEnclose != null) {
                s.append(valueEnclose)
            }
        }
        return s.toString()
    }
}

class ElementEnd(name: String, lineNumber: Int = 1) : ElementItem(name, lineNumber) {

    override fun toString(): String {
        return "</$name>"
    }
}

abstract class ElementItem(open val name: String, lineNumber: Int) : StreamItem(lineNumber)

class ContentItem(val content: String, lineNumber: Int = 1) : StreamItem(lineNumber) {

    override fun toString(): String {
        return content
    }
}