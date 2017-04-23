package fg.htmlparser.html


class CharReel(val chars: String, var index: Int = 0) {

    private var _soFar: StringBuilder = StringBuilder()
    val soFar: String
        get() = _soFar.toString()
    var lineCount: Int = 1
        private set

    init {
        if (index > 0) {
            _soFar.append(chars.substring(0, index))
        }
    }


    val current: Char
        get() = chars[index - 1]

    val next: Char?
        get() {
            if (index <= chars.lastIndex) {
                return chars[index]
            } else {
                return null
            }
        }

    fun nextMatches(vararg matches: Char): Boolean {
        return nextMatches(matches.toList())
    }

    fun nextMatches(matches: Collection<Char>): Boolean {
        val next = next
        return matches.any { it == next }
    }

    fun atEnd(): Boolean {
        return index >= chars.length - 1
    }

    fun forward(): Char {
        val c = chars[index]
        if (c == NEW_LINE) {
            lineCount++
        }
        _soFar.append(c)
        index++
        return c
    }

    fun forwardUntil(matches: Collection<Char>): String {

        return forwardUntil(*matches.toCharArray())
    }

    fun forwardUntil(vararg matches: Char): String {

        if (matches.any { it == next }) {
            forward()
            return ""
        }

        val result = StringBuilder()
        do {
            index++
            if (current == NEW_LINE) {
                lineCount++
            }
            _soFar.append(current)
            result.append(current)
        } while (next != null && !matches.any { it == next })

        if (index < chars.length) {
            forward()
        }

        return result.toString()
    }

    fun forwardWhile(vararg matches: Char): String {

        if (!matches.any { it == next }) {
            return ""
        }

        val result = StringBuilder()
        do {
            index++
            if (current == NEW_LINE) {
                lineCount++
            }
            _soFar.append(current)
            result.append(current)
        } while (matches.any { it == next })

        return result.toString()
    }

    companion object {
        private val NEW_LINE = '\n'
    }
}



