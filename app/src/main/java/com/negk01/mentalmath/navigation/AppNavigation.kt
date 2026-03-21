//package com.negk01.mentalmath.navigation
//
//import androidx.compose.runtime.Composable
//import com.negk01.mentalmath.ui.screens.home.HomeScreen
//
//@Composable
//fun AppNavigation() {
//    HomeScreen()
//}

package com.negk01.mentalmath.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.negk01.mentalmath.ui.screens.home.HomeScreen
import com.negk01.mentalmath.ui.screens.config.ConfigScreen
import androidx.compose.runtime.getValue
import com.negk01.mentalmath.ui.screens.results.ResultsScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {

        composable(Routes.HOME) {
            HomeScreen(navController,
                currentRoute = currentRoute)
        }

        composable(Routes.CONFIG) {
            ConfigScreen(navController,
                currentRoute = currentRoute)
        }

        composable(Routes.RESULTS) {
            ResultsScreen(navController,
                currentRoute = currentRoute)
        }
    }
}