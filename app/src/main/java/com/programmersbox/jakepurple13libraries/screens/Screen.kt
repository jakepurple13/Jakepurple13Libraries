package com.programmersbox.jakepurple13libraries.screens

import androidx.navigation.NavController

enum class Screen {
    Main,
    PatternInput,
    GroupButton,
    DiamondLoader,
    Poker
}

fun NavController.navigate(screen: Screen) = navigate(screen.name)