package com.programmersbox.jakepurple13libraries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.programmersbox.jakepurple13libraries.screens.Screen
import com.programmersbox.jakepurple13libraries.screens.composable
import com.programmersbox.jakepurple13libraries.screens.navigate
import com.programmersbox.jakepurple13libraries.ui.theme.Jakepurple13LibrariesTheme
import com.programmersbox.navigationcomposeutils.LocalNavController
import eu.wewox.pagecurl.ExperimentalPageCurlApi
import eu.wewox.pagecurl.page.PageCurl
import eu.wewox.pagecurl.page.rememberPageCurlState

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

@Composable
fun AppUi() {
    val navController = LocalNavController.current
    NavHost(navController = navController, startDestination = Screen.Main.name) {
        Screen.values().forEach { composable(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = LocalNavController.current
    ScaffoldTop(screen = Screen.Main, showBackButton = false) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = it
        ) {
            items(Screen.values().dropWhile { s -> s == Screen.Main }) {
                OutlinedButton(onClick = { navController.navigate(it) }) { Text(it.name) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPageCurlApi::class)
@Composable
fun PageCurlScreen() {
    ScaffoldTop(screen = Screen.PageCurl) {
        val pages = listOf("One", "Two", "Three")
        val state = rememberPageCurlState(max = pages.size)
        PageCurl(
            state = state,
            modifier = Modifier.padding(it)
        ) { index ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("This is just because I want to see this")
                    Text(text = pages[index])
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