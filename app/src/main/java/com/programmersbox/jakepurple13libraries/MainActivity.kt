package com.programmersbox.jakepurple13libraries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.programmersbox.jakepurple13libraries.screens.GroupButtonScreen
import com.programmersbox.jakepurple13libraries.screens.Screen
import com.programmersbox.jakepurple13libraries.screens.navigate
import com.programmersbox.jakepurple13libraries.ui.theme.Jakepurple13LibrariesTheme
import com.programmersbox.jakepurple13libraries.ui.theme.LocalNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jakepurple13LibrariesTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppUi()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUi() {
    val navController = LocalNavController.current
    NavHost(navController = navController, startDestination = Screen.Main.name) {
        composable(Screen.PatternInput.name) { PatternScreen() }
        composable(Screen.GroupButton.name) { GroupButtonScreen() }
        composable(Screen.Main.name) {
            ScaffoldTop(screen = Screen.Main, showBackButton = false) {
                LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = it) {
                    items(Screen.values().dropWhile { it == Screen.Main }) {
                        OutlinedButton(onClick = { navController.navigate(it) }) { Text(it.name) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Jakepurple13LibrariesTheme {
        AppUi()
    }
}