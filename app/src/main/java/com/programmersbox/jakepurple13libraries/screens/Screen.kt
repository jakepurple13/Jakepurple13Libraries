package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.programmersbox.jakepurple13libraries.MainScreen
import com.programmersbox.jakepurple13libraries.PageCurlScreen
import com.programmersbox.jakepurple13libraries.PatternScreen

enum class Screen(val content: @Composable (NavBackStackEntry) -> Unit) {
    Main({ MainScreen() }),
    PatternInput({ PatternScreen() }),
    GroupButton({ GroupButtonScreen() }),
    DiamondLoader({ DiamondLoaderScreen() }),
    Poker({ Poker() }),
    Yahtzee({ YahtzeeScreen() }),
    PageCurl({ PageCurlScreen() })
}

fun NavController.navigate(screen: Screen) = navigate(screen.name)

fun NavGraphBuilder.composable(
    route: Screen,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
) = composable(route.name, arguments, deepLinks, route.content)