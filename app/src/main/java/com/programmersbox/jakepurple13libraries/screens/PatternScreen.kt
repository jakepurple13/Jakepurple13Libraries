package com.programmersbox.jakepurple13libraries

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.programmersbox.jakepurple13libraries.screens.Screen
import com.programmersbox.patterninput.PatternInput
import com.programmersbox.patterninput.PatternInputDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternScreen() {
    ScaffoldTop(screen = Screen.PatternInput) {
        var wordGuess by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(wordGuess)
            PatternInput(
                options = listOf("h", "e", "l", "l", "o", "!", "!"),
                modifier = Modifier.size(height = 250.dp, width = 250.dp),
                optionToString = { it },
                colors = PatternInputDefaults.defaultColors(
                    dotsColor = MaterialTheme.colorScheme.primary,
                    linesColor = MaterialTheme.colorScheme.onSurface,
                    letterColor = MaterialTheme.colorScheme.primary,
                ),
                sensitivity = 100f,
                dotsSize = 50.sp.value,
                dotCircleSize = 50f * 1.5f,
                linesStroke = 50f,
                circleStroke = Stroke(width = 4.dp.value),
                animationDuration = 200,
                animationDelay = 100,
                onStart = {
                    wordGuess = ""
                    wordGuess = it.id
                },
                onDotRemoved = { wordGuess = wordGuess.removeSuffix(it.id) },
                onDotConnected = { wordGuess = "$wordGuess${it.id}" },
                onResult = { /*Does a final thing*/ }
            )
        }
    }
}