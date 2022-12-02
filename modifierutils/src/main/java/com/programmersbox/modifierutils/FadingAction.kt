package com.programmersbox.modifierutils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha

enum class FadingAction {
    Fade, None;

    operator fun not() = when (this) {
        Fade -> None
        None -> Fade
    }
}

fun Modifier.fadingQuickAction(
    key: FadingAction,
    animationSpec: AnimationSpec<Float> = spring(),
    onAnimationEnd: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) = composed {
    val alphaValue = remember { Animatable(0f) }

    LaunchedEffect(key) {
        when (key) {
            FadingAction.Fade -> alphaValue
            FadingAction.None -> null
        }?.let { animatable ->
            animatable.animateTo(1f, animationSpec)
            animatable.animateTo(0f, animationSpec)
            onAnimationEnd()
        }
    }

    Box(
        modifier = Modifier
            .alpha(alphaValue.value)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = content
    )

    this
}