package com.programmersbox.randomutils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * @return a random color
 */
@JvmOverloads
fun Random.nextColor(
    alpha: Int = nextInt(0, 255),
    red: Int = nextInt(0, 255),
    green: Int = nextInt(0, 255),
    blue: Int = nextInt(0, 255)
): Color = Color(red, green, blue, alpha)

/**
 * @return a random color
 */
@JvmOverloads
fun Random.nextColorInt(
    alpha: Int = nextInt(0, 255),
    red: Int = nextInt(0, 255),
    green: Int = nextInt(0, 255),
    blue: Int = nextInt(0, 255)
): Int = android.graphics.Color.argb(alpha, red, green, blue)

suspend fun randomNumberAnimation(
    newValue: Int,
    valueChange: (Int) -> Unit,
    delayAmountMs: Long = 50L,
    randomCount: Int = 5
) {
    repeat(randomCount) {
        delay(delayAmountMs)
        valueChange(Random.nextInt(1..6))
    }
    valueChange(newValue)
}

@Composable
fun <T> randomItemAnimation(
    initialValue: T,
    newValue: () -> T,
    randomValue: (Int) -> T,
    randomCount: Int = 5,
    delayAmountMs: Long = 50L,
    vararg keys: Any
) = produceState(initialValue = initialValue, keys = keys) {
    val endValue = newValue()
    repeat(randomCount) {
        delay(delayAmountMs)
        value = randomValue(it)
    }
    value = endValue
}

@Composable
fun randomNumberAnimation(
    initialValue: Int,
    newValue: Int,
    randomCount: Int = 5,
    delayAmountMs: Long = 50L,
    vararg keys: Any
) = randomItemAnimation(
    initialValue = initialValue,
    newValue = { newValue },
    randomValue = { Random.nextInt(1..6) },
    randomCount = randomCount,
    delayAmountMs = delayAmountMs,
    keys = keys
)