package com.programmersbox.modifierutils

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Color.animate() = animateColorAsState(this)

@Composable
fun ColorScheme.animate() = copy(
    primary = primary.animate().value,
    onPrimary = onPrimary.animate().value,
    primaryContainer = primaryContainer.animate().value,
    onPrimaryContainer = onPrimaryContainer.animate().value,
    inversePrimary = inversePrimary.animate().value,
    secondary = secondary.animate().value,
    onSecondary = onSecondary.animate().value,
    secondaryContainer = secondaryContainer.animate().value,
    onSecondaryContainer = onSecondaryContainer.animate().value,
    tertiary = tertiary.animate().value,
    onTertiary = onTertiary.animate().value,
    tertiaryContainer = tertiaryContainer.animate().value,
    onTertiaryContainer = onTertiaryContainer.animate().value,
    background = background.animate().value,
    onBackground = onBackground.animate().value,
    surface = surface.animate().value,
    onSurface = onSurface.animate().value,
    surfaceVariant = surfaceVariant.animate().value,
    onSurfaceVariant = onSurfaceVariant.animate().value,
    surfaceTint = surfaceTint.animate().value,
    inverseSurface = inverseSurface.animate().value,
    inverseOnSurface = inverseOnSurface.animate().value,
    error = error.animate().value,
    onError = onError.animate().value,
    errorContainer = errorContainer.animate().value,
    onErrorContainer = onErrorContainer.animate().value,
    outline = outline.animate().value,
    outlineVariant = outlineVariant.animate().value,
    scrim = scrim.animate().value
)

@Composable
fun Colors.animate() = copy(
    primary = primary.animate().value,
    primaryVariant = primaryVariant.animate().value,
    onPrimary = onPrimary.animate().value,
    secondary = secondary.animate().value,
    secondaryVariant = secondaryVariant.animate().value,
    onSecondary = onSecondary.animate().value,
    background = background.animate().value,
    onBackground = onBackground.animate().value,
    surface = surface.animate().value,
    onSurface = onSurface.animate().value,
    onError = onError.animate().value,
    error = error.animate().value,
    isLight = isLight
)