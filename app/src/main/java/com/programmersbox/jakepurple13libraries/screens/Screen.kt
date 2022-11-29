package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable

enum class Screen {
    Main,
    PatternInput,
    GroupButton,
    DiamondLoader,
    Poker,
    PageCurl
}

fun NavController.navigate(screen: Screen) = navigate(screen.name)

fun NavGraphBuilder.composable(
    route: Screen,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(route.name, arguments, deepLinks, content)