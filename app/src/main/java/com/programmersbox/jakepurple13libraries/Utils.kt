package com.programmersbox.jakepurple13libraries

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.programmersbox.jakepurple13libraries.screens.Screen
import com.programmersbox.jakepurple13libraries.ui.theme.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTop(
    screen: Screen,
    showBackButton: Boolean = true,
    topAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    bottomBar: @Composable () -> Unit = {},
    topBarActions: @Composable RowScope.() -> Unit = {},
    block: @Composable (PaddingValues) -> Unit
) {
    val navController = LocalNavController.current
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(screen.name) },
                navigationIcon = if (showBackButton) {
                    { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } }
                } else {
                    {}
                },
                actions = topBarActions,
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        containerColor = containerColor,
        bottomBar = bottomBar,
        content = block
    )
}