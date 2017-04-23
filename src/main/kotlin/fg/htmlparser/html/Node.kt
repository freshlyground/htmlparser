package fg.htmlparser.html

abstract class Node(val lineNumber: Int) {

    var parent: Element? = null
    var index: Int = 0

    abstract fun content(): String

    abstract fun toXmlString(): String
}