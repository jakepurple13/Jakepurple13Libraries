package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.programmersbox.groupbutton.GroupButton
import com.programmersbox.groupbutton.GroupButtonModel
import com.programmersbox.jakepurple13libraries.ScaffoldTop

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
