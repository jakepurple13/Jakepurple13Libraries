package com.programmersbox.randomutils

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

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