package com.programmersbox.cards

import org.junit.Assert.assertEquals
import org.junit.Test

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

    @Test
    fun deskTesting() {
        Deck.defaultDeck().apply {
            fun <T> T.printDeck() = let { println(this) }
            val random = Card.RandomCard
            println(this)
            addDeck(Deck.defaultDeck()).printDeck()
            addCards(listOf(random)).printDeck()
            println(randomCard)
            println(draw())
            println(-this)
            println(deck)
            println(this[1])
            println(firstCard)
            println(middleCard)
            println(lastCard)
            println(isEmpty)
            println(isNotEmpty)
            println(size)
            println(remove(random))
            println(draw(3))
            addCard(1, random).printDeck()
            addCard(1 to random).printDeck()
            add(random).printDeck()
            addCards(arrayOf(random)).printDeck()
            println(findCardLocation(random))
            sortDeck(compareBy { it.value }).printDeck()
            sortDeckBy { it.value }.printDeck()
            remove(random, random).printDeck()
            random { it.value == 10 }.printDeck()
            println(randomDraw { it.value == 10 }).printDeck()
        }
    }
}