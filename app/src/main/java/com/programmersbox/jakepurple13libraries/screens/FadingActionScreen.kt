package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.programmersbox.jakepurple13libraries.ScaffoldTop
import com.programmersbox.modifierutils.FadingAction
import com.programmersbox.modifierutils.fadingQuickAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FadingActionScreen() {
    ScaffoldTop(screen = Screen.FadingAction) { p ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = p
        ) {
            items(
                listOf(
                    Icons.Default.Abc,
                    Icons.Default.Add,
                    Icons.Default.ArrowBack,
                    Icons.Default.ArrowForward,
                    Icons.Default.PlayArrow,
                    Icons.Default.Pause
                )
            ) {
                var action by remember { mutableStateOf(FadingAction.None) }
                OutlinedButton(
                    onClick = { action = !action },
                    modifier = Modifier.fadingQuickAction(
                        action,
                        animationSpec = tween(500),
                        onAnimationEnd = { action = !action }
                    ) { Icon(it, null, Modifier.align(Alignment.Center)) }
                ) { Text("Fade Action ${it.name}") }
            }
        }
    }
}