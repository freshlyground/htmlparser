package fg.htmlparser.html

class HtmlStream(val list: List<StreamItem>) {

    private var cursor: Int = 0

    val current: StreamItem
        get() = list[cursor]

    val currentElementStart: ElementStart
        get() = list[cursor] as ElementStart

    val currentElementEnd: ElementEnd
        get() = list[cursor] as ElementEnd

    operator fun get(index: Int): StreamItem {
        return list[index]
    }

    fun atEnd(): Boolean {
        return cursor > list.lastIndex
    }

    fun forward(): StreamItem {
        val item = list[cursor]
        cursor++
        return item
    }

    fun forwardUntil(predicate: (item: StreamItem) -> Boolean): List<StreamItem> {

        val result: MutableList<StreamItem> = arrayListOf()
        while (cursor < list.size && !predicate.invoke(list[cursor])) {
            result.add(list[cursor])
            cursor++
        }
        return result
    }

    fun forwardUntilElementStart(): List<StreamItem> {
        return forwardUntil { it is ElementStart }
    }

    fun forwardUntilElementStart(predicate: (item: ElementStart) -> Boolean): List<StreamItem> {

        val result: MutableList<StreamItem> = arrayListOf()
        while (cursor < list.size) {
            val currItem = list[cursor]
            if (currItem is ElementStart) {
                if (predicate.invoke(currItem)) {
                    break
                }
            }
            result.add(list[cursor])
            cursor++
        }
        return result
    }

    fun forwardUntilElementEnd(predicate: (item: ElementEnd) -> Boolean): List<StreamItem> {

        val result: MutableList<StreamItem> = arrayListOf()
        while (cursor < list.size) {
            val currItem = list[cursor]
            if (currItem is ElementEnd) {
                if (predicate.invoke(currItem)) {
                    break
                }
            }
            result.add(list[cursor])
            cursor++
        }
        return result
    }

    fun forwardUntilElementEnd(): List<StreamItem> {
        return forwardUntil { it is ElementEnd }
    }

    override fun toString(): String {
        val s = StringBuilder()
        for (i in 0..list.lastIndex) {
            s.append(list[i])
        }
        return s.toString()
    }

    companion object {

        fun parse(html: String): HtmlStream {
            return HtmlStreamParser(html).parse()
        }
    }
}

