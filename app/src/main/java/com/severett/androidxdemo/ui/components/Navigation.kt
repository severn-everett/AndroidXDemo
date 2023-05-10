package com.severett.androidxdemo.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.severett.androidxdemo.ui.theme.europaFamily
import com.severett.androidxdemo.ui.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentTitle = navBackStackEntry?.destination
        ?.route
        ?.let { route -> Constants.NavItems.find { navItem -> navItem.route == route } }
        ?.let { navItem -> stringResource(id = navItem.labelId) }
        ?: ""
    CenterAlignedTopAppBar(title = {
        Text(
            text = currentTitle,
            fontFamily = europaFamily,
            fontWeight = FontWeight.Bold
        )
    })
}

@Composable
fun MainNavigation(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Constants.StartingItem,
        modifier = Modifier.padding(paddingValues = padding)
    ) {
        Constants.NavItems.forEach { navItem ->
            composable(navItem.route, content = navItem.content)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Constants.NavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = { navController.navigate(navItem.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.drawableId),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = navItem.labelId),
                        fontFamily = europaFamily,
                        fontSize = 13.sp
                    )
                },
                alwaysShowLabel = true,
            )
        }
    }
}
