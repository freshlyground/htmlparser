package fg.htmlparser.html

import org.junit.Assert.assertEquals
import org.junit.Test

class CharReelTest {

    @Test
    fun forward_from_index_0() {
        val reel = CharReel("abc", 0)
        assertEquals('a', reel.forward())
        assertEquals("a", reel.soFar)
    }

    @Test
    fun forward_from_index_1() {
        val reel = CharReel("abc", 1)
        assertEquals('b', reel.forward())
        assertEquals("ab", reel.soFar)
    }

    @Test
    fun forward_from_lastIndex() {
        val reel = CharReel("abc", 2)
        assertEquals('c', reel.forward())
        assertEquals("abc", reel.soFar)
    }

    @Test
    fun forwardUntil() {

        val reel = CharReel("abc ", 0)
        assertEquals("abc", reel.forwardUntil(' '))
        assertEquals(4, reel.index)
    }

    @Test
    fun forwardUntil_match_at_index_0() {

        val reel = CharReel("<html>", 0)
        assertEquals("", reel.forwardUntil('<'))
        assertEquals("html", reel.forwardUntil('>'))
    }

    @Test
    fun forwardUntil_no_match_and_end_reached() {

        val reel = CharReel("abc", 0)
        assertEquals("abc", reel.forwardUntil(' '))
        assertEquals(3, reel.index)
    }

    @Test
    fun soFar() {
        assertEquals("", CharReel("abc", -1).soFar)
        assertEquals("", CharReel("abc", 0).soFar)
        assertEquals("a", CharReel("abc", 1).soFar)
    }

    @Test
    fun index_0() {
        val reel = CharReel("abc ", 0)
        assertEquals(0, reel.index)
        assertEquals("", reel.soFar)
        assertEquals('a', reel.next)
        assertEquals("abc", reel.forwardUntil(' '))
    }

    @Test
    fun index_1() {
        val reel = CharReel("abc ", 1)
        assertEquals(1, reel.index)
        assertEquals("a", reel.soFar)
        assertEquals('a', reel.current)
        assertEquals('b', reel.next)
        assertEquals("bc", reel.forwardUntil(' '))
    }

    @Test
    fun index_second_last() {
        val reel = CharReel("abc ", 2)
        assertEquals(2, reel.index)
        assertEquals("ab", reel.soFar)
        assertEquals('b', reel.current)
        assertEquals('c', reel.next)
        assertEquals("c", reel.forwardUntil(' '))
    }


    @Test
    fun forwardWhile() {
        val reel = CharReel("before    after", 6)
        assertEquals("    ", reel.forwardWhile(' '))
        assertEquals(' ', reel.current)
        assertEquals('a', reel.next)
    }

    @Test
    fun forwardWhile_no_match() {
        val reel = CharReel("before    after", 5)
        assertEquals("", reel.forwardWhile(' '))
        assertEquals('r', reel.current)
        assertEquals('e', reel.next)
    }

    @Test
    fun forwardUntil_two_times() {
        val reel = CharReel("abc 123", 0)
        assertEquals("abc", reel.forwardUntil(' '))
        assertEquals("123", reel.forwardUntil(' '))
    }

    @Test
    fun lineCount_when_forwardUntil() {
        val reel = CharReel("line1\nline2\nline3\n", 0)
        assertEquals("line1", reel.forwardUntil('\n'))
        assertEquals(1, reel.lineCount)
        assertEquals("line2", reel.forwardUntil('\n'))
        assertEquals(2, reel.lineCount)
        assertEquals("line3", reel.forwardUntil('\n'))
        assertEquals(3, reel.lineCount)
    }

    @Test
    fun lineCount_when_forwardWhile() {
        val reel = CharReel("aaa\nbbb\nccc\n", 0)
        assertEquals("aaa", reel.forwardWhile('a'))
        assertEquals(1, reel.lineCount)
        reel.forward()
        assertEquals("bbb", reel.forwardWhile('b'))
        assertEquals(2, reel.lineCount)
        reel.forward()
        assertEquals("ccc", reel.forwardWhile('c'))
        assertEquals(3, reel.lineCount)
    }

}