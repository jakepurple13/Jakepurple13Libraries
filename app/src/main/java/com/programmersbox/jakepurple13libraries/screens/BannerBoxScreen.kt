package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.programmersbox.jakepurple13libraries.ScaffoldTop
import com.programmersbox.modifierutils.BannerBox
import com.programmersbox.modifierutils.ComponentState
import com.programmersbox.modifierutils.combineClickableWithIndication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerBoxScreen() {
    var showBanner by remember { mutableStateOf(false) }
    ScaffoldTop(screen = Screen.BannerBox) { p ->
        BannerBox(
            modifier = Modifier.padding(p),
            showBanner = showBanner,
            banner = {
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter),
                    shape = MaterialTheme.shapes.medium.copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp)),
                    tonalElevation = 4.dp,
                    shadowElevation = 10.dp
                ) {
                    ListItem(
                        leadingContent = { Icon(Icons.Default.PlayArrow, null) },
                        overlineText = { Text("Tada!") },
                        headlineText = { Text("A Banner Box!") },
                        supportingText = { Text("Amazing!") }
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(p)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = ButtonDefaults.outlinedShape,
                    modifier = Modifier
                        .clip(ButtonDefaults.outlinedShape)
                        .combineClickableWithIndication(onLongPress = { showBanner = it == ComponentState.Pressed })
                ) {
                    Text(
                        "Show Box",
                        modifier = Modifier.padding(ButtonDefaults.ContentPadding)
                    )
                }
            }
        }
    }
}