package com.negk01.mentalmath.navigation

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.negk01.mentalmath.data.local.db.DatabaseProvider
import com.negk01.mentalmath.data.repository.GameRecordRepositoryImpl
import com.negk01.mentalmath.data.repository.SettingsRepositoryImpl
import com.negk01.mentalmath.presentation.config.ConfigViewModel
import com.negk01.mentalmath.presentation.config.ConfigViewModelFactory
import com.negk01.mentalmath.presentation.game.GameViewModel
import com.negk01.mentalmath.presentation.game.GameViewModelFactory
import com.negk01.mentalmath.presentation.history.HistoryViewModel
import com.negk01.mentalmath.presentation.history.HistoryViewModelFactory
import com.negk01.mentalmath.presentation.home.HomeViewModel
import com.negk01.mentalmath.presentation.home.HomeViewModelFactory
import com.negk01.mentalmath.ui.screens.config.ConfigScreen
import com.negk01.mentalmath.ui.screens.game.GameScreen
import com.negk01.mentalmath.ui.screens.history.HistoryScreen
import com.negk01.mentalmath.ui.screens.home.HomeScreen
import com.negk01.mentalmath.ui.screens.results.ResultsScreen
import com.negk01.mentalmath.ui.theme.MentalMathTheme
import com.negk01.mentalmath.ui.utils.toLocaleListCompat

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val database = DatabaseProvider.getDatabase(context)

    val settingsRepository = SettingsRepositoryImpl(database.settingsDao())
    val gameRecordRepository = GameRecordRepositoryImpl(database.gameRecordDao())

    val configViewModel: ConfigViewModel = viewModel(
        factory = ConfigViewModelFactory(
            settingsRepository = settingsRepository,
            gameRecordRepository = gameRecordRepository
        )
    )

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(gameRecordRepository)
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(gameRecordRepository)
    )

    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(gameRecordRepository)
    )

    val configUiState by configViewModel.uiState.collectAsState()

    LaunchedEffect(configUiState.languagePreference) {
        AppCompatDelegate.setApplicationLocales(
            configUiState.languagePreference.toLocaleListCompat()
        )
    }

    MentalMathTheme(themePreference = configUiState.themePreference) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    viewModel = homeViewModel,
                    onStartGame = {
                        gameViewModel.startGame(configUiState.selectedDifficulty)
                        navController.navigate(Routes.GAME)
                    }
                )
            }

            composable(Routes.HISTORY) {
                HistoryScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    viewModel = historyViewModel
                )
            }

            composable(Routes.CONFIG) {
                ConfigScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    viewModel = configViewModel
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
}
