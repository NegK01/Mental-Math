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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.negk01.mentalmath.presentation.game.GameViewModel
import com.negk01.mentalmath.ui.screens.config.ConfigScreen
import com.negk01.mentalmath.ui.screens.game.GameScreen
import com.negk01.mentalmath.ui.screens.history.HistoryScreen
import com.negk01.mentalmath.ui.screens.home.HomeScreen
import com.negk01.mentalmath.ui.screens.results.ResultsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val gameViewModel: GameViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(Routes.CONFIG) {
            ConfigScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                navController = navController,
                viewModel = gameViewModel
            )
        }

        composable(Routes.RESULTS) {
            ResultsScreen(
                navController = navController,
                currentRoute = currentRoute,
                sessionResult = gameViewModel.sessionResult,
                onPlayAgain = {
                    gameViewModel.restartGame()
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.RESULTS) { inclusive = true }
                    }
                }
            )
        }
    }
}