package com.programmersbox.jakepurple13libraries.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.programmersbox.groupbutton.GroupButton
import com.programmersbox.groupbutton.GroupButtonModel
import com.programmersbox.jakepurple13libraries.ScaffoldTop
import com.programmersbox.jakepurple13libraries.ui.theme.Jakepurple13LibrariesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupButtonScreen() {
    ScaffoldTop(screen = Screen.GroupButton) {
        Column(modifier = Modifier.padding(it)) {
            var item by remember { mutableStateOf(1) }

            GroupButton(
                selected = item,
                options = (0..2).map {
                    GroupButtonModel(
                        item = it,
                        iconContent = { Text(it.toString()) }
                    )
                },
                onClick = { item = it }
            )
        }
    }
}

@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 33
)
@Composable
fun GroupButtonPreview() {
    Jakepurple13LibrariesTheme {
        GroupButtonScreen()
    }
}