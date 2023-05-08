package com.severett.androidxdemo.ui.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

data class NavItem(
    val labelId: Int,
    val drawableId: Int,
    val route: String,
    val content: @Composable (NavBackStackEntry) -> Unit
)
