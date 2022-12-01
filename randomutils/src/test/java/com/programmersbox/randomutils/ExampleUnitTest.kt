package com.programmersbox.randomutils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private fun getDictionaryList(): List<String> {
        return File("/usr/share/dict/words")
            .readLines()
            .filterNot { it.contains("-") }
    }

    @Test
    fun anagramTest() {
        val d = getDictionaryList()
        val w = d.filter { it.length == 7 }.random()
        val words = d
            .filter { it.length >= 3 && it isAnagramOf w }
            .distinctBy { it.lowercase() }

        println(w)
        println(words)

        val wordYellow = "yellow"
        val anagramYellow = "lowly"
        val failedAnagramYellow = "lay"
        assert(wordYellow hasAnagram anagramYellow)
        assert(!(failedAnagramYellow isAnagramOf wordYellow))
    }
}