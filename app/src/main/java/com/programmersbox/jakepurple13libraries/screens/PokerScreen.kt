package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.programmersbox.cards.*
import com.programmersbox.jakepurple13libraries.ScaffoldTop
import kotlinx.coroutines.launch

enum class PokerState { Start, Swap, End }

class PokerViewModel : ViewModel() {

    private val deck = Deck.defaultDeck().also {
        it.addDeckListener {
            onDraw { _, size ->
                if (size == 0) {
                    it.addDeck(Deck.defaultDeck())
                    it.shuffle()
                }
            }
        }
        it.shuffle()
    }

    val hand = mutableStateListOf<Card>()
    var currentAmount by mutableStateOf(500)
    var currentBet by mutableStateOf(1)
    var state by mutableStateOf(PokerState.Start)
    val cardsToDiscard = mutableStateListOf<Card>()

    private fun draw() {
        hand.removeAll(cardsToDiscard)
        while (hand.size != 5) {
            hand.add(deck.draw())
        }
    }

    private fun reset() {
        hand.clear()
        hand.addAll(deck.draw(5))
    }

    fun start() {
        currentAmount -= currentBet
        state = PokerState.Swap
        reset()
    }

    fun swap() {
        draw()
        state = PokerState.End
    }

    fun end(snackbarHostState: SnackbarHostState) {
        val winnings = PokerHand.values().first { it.check(hand) }.let { it.initialWinning * currentBet }
        viewModelScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            if (winnings > 0) {
                snackbarHostState.showSnackbar("Won \$$winnings")
            } else {
                snackbarHostState.showSnackbar("Lost \$$currentBet")
            }
        }
        currentAmount += winnings
        cardsToDiscard.clear()
        hand.clear()
        state = PokerState.Start
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Poker(vm: PokerViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    ScaffoldTop(
        screen = Screen.Poker,
        bottomBar = {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy((-25).dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (vm.hand.isEmpty()) {
                    items(5) { EmptyCard() }
                } else {
                    items(vm.hand) {
                        PlayingCard(
                            it,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    animateColorAsState(if (it in vm.cardsToDiscard) Color.Red else Color.Transparent).value,
                                    RoundedCornerShape(2.dp)
                                )
                        ) { if (it in vm.cardsToDiscard) vm.cardsToDiscard.remove(it) else vm.cardsToDiscard.add(it) }
                    }
                }
            }
        },
        snackbarHostState = snackbarHostState,
        topBarActions = { Text("\$${vm.currentAmount}") }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
        ) {
            val canCheck = vm.hand.size == 5
            val s = LocalTextStyle.current
            val defaultColor = s.color

            val check = if (canCheck) {
                PokerHand.values().firstOrNull { it.check(vm.hand) }
            } else {
                null
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                PokerHand.values().forEach { h ->
                    item {
                        Text(
                            h.name,
                            style = s.copy(animateColorAsState(targetValue = if (check == h) Color.Red else defaultColor).value)
                        )
                    }

                    items(5) {
                        Text(
                            "${h.initialWinning * (it + 1)}",
                            style = s.copy(animateColorAsState(targetValue = if (vm.currentBet == (it + 1)) Color.Green else defaultColor).value),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            AnimatedVisibility(vm.state == PokerState.Start) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { vm.currentBet = (vm.currentBet - 1).coerceAtLeast(1) }) {
                        Icon(Icons.Default.RemoveCircle, null)
                    }
                    Text("\$${vm.currentBet}")
                    IconButton(onClick = { vm.currentBet = (vm.currentBet + 1).coerceAtMost(5) }) {
                        Icon(Icons.Default.AddCircle, null)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                if (vm.state == PokerState.Start) {
                    Button(
                        onClick = { vm.start() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Play") }
                }

                if (vm.state == PokerState.Swap) {
                    Button(
                        onClick = { vm.swap() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Draw") }
                }

                if (vm.state == PokerState.End) {
                    Button(
                        onClick = { vm.end(snackbarHostState) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Play Again") }
                }
            }
        }
    }
}

enum class PokerHand(val rank: Int, val initialWinning: Int) {
    RoyalFlush(9, 250) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            if (h[1].value == 10) {
                if (h[2].value == 11) {
                    if (h[3].value == 12) {
                        if (h[4].value == 13) {
                            if (h[0].value == 1) {
                                if (Straight.check(h) && Flush.check(h)) {
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return false
        }
    },
    StraightFlush(8, 50) {
        override fun check(hand: List<Card>): Boolean = Straight.check(hand) && Flush.check(hand)
    },
    FourOfAKind(7, 25) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            var acceptable = false
            var count = 0
            val numberCount = h[3].value
            for (element in h) {
                if (element.value == numberCount) {
                    count++
                }
            }
            if (count == 4) {
                acceptable = true
            }

            return acceptable
        }
    },
    FullHouse(6, 9) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            var count = 1
            var found = false
            var found1 = false
            for (i in 1 until h.size) {
                if (h[i].compareTo(h[i - 1]) == 0) {
                    count++
                } else {
                    if (count == 3) {
                        found1 = true
                    } else if (count == 2) {
                        found = true
                    }
                    count = 1
                }

            }

            if (count == 3) {
                found1 = true
            } else if (count == 2) {
                found = true
            }

            return found && found1
        }
    },
    Flush(5, 6) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            for (i in 1 until h.size) {
                if (h[i].suit != h[i - 1].suit) {
                    return false
                }
            }
            return true
        }
    },
    Straight(4, 4) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            var count = 0
            var value: Int
            for (i in 0 until h.size - 1) {
                value = h[i].value
                if (value == 1) {
                    if (h[i + 1].value == 2) {
                        value = 1
                    } else if (h[i + 1].value == 10) {
                        value = 9
                    }
                }
                if (value + 1 == h[i + 1].value) {
                    count++
                }
            }

            return count == 4
        }
    },
    ThreeOfAKind(3, 3) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            var acceptable = false
            var count = 1
            var hold = false
            for (i in 1 until h.size) {

                if (h[i].compareTo(h[i - 1]) == 0) {
                    count++
                    hold = true
                } else if (hold) {
                    break
                }

            }

            if (count == 3) {
                acceptable = true
            }

            return acceptable
        }
    },
    TwoPair(2, 2) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            var count = 1
            var found = false
            var found1 = false
            var i = 1
            while (i < h.size) {
                if (h[i].compareTo(h[i - 1]) == 0) {
                    count++
                    i++
                }
                if (count == 2 && found1) {
                    found = true
                    count = 1
                } else if (count == 2) {
                    found1 = true
                    count = 1
                }
                i++


            }

            if (count == 2) {
                found1 = true
            } else if (count == 2 && found1) {
                found = true
            }

            return found && found1
        }
    },
    Pair(1, 1) {
        override fun check(hand: List<Card>): Boolean {
            val h = hand.sortedBy { it.value }
            var acceptable = false
            var count = 0
            for (i in 1 until h.size) {
                //if (h[i].compareTo(h[i - 1]) == 0) {
                val valueMin = if (jacksOrBetter) 11 else 1
                if (h[i].compareTo(h[i - 1]) == 0 && h[i].value > valueMin || h[i].value == 1) {
                    count++
                }

            }

            if (count == 1) {
                acceptable = true
            }

            return acceptable
        }
    },
    HighCard(0, 0) {
        override fun check(hand: List<Card>): Boolean = true
    };

    abstract fun check(hand: List<Card>): Boolean

    companion object {
        var jacksOrBetter = false
    }
}