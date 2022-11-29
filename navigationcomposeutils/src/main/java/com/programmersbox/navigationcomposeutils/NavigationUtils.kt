package com.programmersbox.navigationcomposeutils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val LocalNavController = staticCompositionLocalOf<NavHostController> { error("No NavController Found!") }

@Composable
fun ProvideNavController(
    navController: NavHostController = rememberNavController(),
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalNavController provides navController,
    content = content
)
