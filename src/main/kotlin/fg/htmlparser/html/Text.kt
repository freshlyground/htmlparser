package fg.htmlparser.html

class Text(val value: String, lineNumber: Int = 1) : Node(lineNumber) {

    override fun content(): String {
        return value
    }

    override fun toString(): String {
        return value
    }

    override fun toXmlString(): String {
        return value
    }
}