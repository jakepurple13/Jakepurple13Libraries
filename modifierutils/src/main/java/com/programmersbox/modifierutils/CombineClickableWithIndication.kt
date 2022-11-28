package com.programmersbox.modifierutils

import androidx.compose.foundation.Indication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

enum class ComponentState { Pressed, Released }

@Composable
fun Modifier.combineClickableWithIndication(
    onLongPress: (ComponentState) -> Unit = {},
    onClick: (() -> Unit)? = null,
    onDoubleTap: (() -> Unit)? = null,
    indication: Indication = rememberRipple()
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    indication(
        interactionSource = interactionSource,
        indication = indication
    )
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { onLongPress(ComponentState.Pressed) },
                onPress = {
                    val press = PressInteraction.Press(it)
                    interactionSource.tryEmit(press)
                    tryAwaitRelease()
                    onLongPress(ComponentState.Released)
                    interactionSource.tryEmit(PressInteraction.Release(press))
                },
                onTap = onClick?.let { c -> { c() } },
                onDoubleTap = onDoubleTap?.let { d -> { d() } }
            )
        }
}