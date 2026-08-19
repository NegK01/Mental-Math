package com.negk01.mentalmath.navigation

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.negk01.mentalmath.presentation.results.ResultsViewModel
import com.negk01.mentalmath.presentation.results.ResultsViewModelFactory
import com.negk01.mentalmath.ui.components.BottomNavBar
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
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    val database = remember(context) { DatabaseProvider.getDatabase(context) }
    val settingsRepository = remember(database) { SettingsRepositoryImpl(database.settingsDao()) }
    val gameRecordRepository = remember(database) { GameRecordRepositoryImpl(database.gameRecordDao()) }

    val configViewModel: ConfigViewModel = viewModel(
        factory = ConfigViewModelFactory(settingsRepository, gameRecordRepository)
    )
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(gameRecordRepository, settingsRepository)
    )
    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(gameRecordRepository)
    )
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(gameRecordRepository)
    )

    val configUiState by configViewModel.uiState.collectAsState()

    LaunchedEffect(configUiState.languagePreference) {
        val targetLocales = configUiState.languagePreference.toLocaleListCompat()
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (targetLocales != currentLocales) {
            AppCompatDelegate.setApplicationLocales(targetLocales)
        }
    }

    MentalMathTheme(themePreference = configUiState.themePreference) {
        val showBottomBar = currentRoute in listOf(Routes.HOME, Routes.HISTORY, Routes.CONFIG)

        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Routes.HOME) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        selectedTheme = configUiState.themePreference,
                        selectedDifficulty = configUiState.selectedDifficulty,
                        onThemeChange = configViewModel::onThemePreferenceSelected,
                        onDifficultyChange = configViewModel::onDifficultySelected,
                        onStartGame = {
                            gameViewModel.startGame(configUiState.selectedDifficulty)
                            navController.navigate(Routes.GAME)
                        }
                    )
                }

                composable(Routes.HISTORY) {
                    HistoryScreen(
                        viewModel = historyViewModel
                    )
                }

                composable(Routes.CONFIG) {
                    ConfigScreen(
                        viewModel = configViewModel
                    )
                }

                composable(Routes.GAME) {
                    GameScreen(
                        viewModel = gameViewModel,
                        onNavigateToResults = {
                            navController.navigate(Routes.RESULTS) {
                                popUpTo(Routes.GAME) { inclusive = true }
                            }
                        },
                        onGameAbandoned = { historyViewModel.resetToTop() },
                        onGoBack = { navController.popBackStack() }
                    )
                }

                composable(Routes.RESULTS) {
                    val resultsViewModel: ResultsViewModel = viewModel(
                        factory = ResultsViewModelFactory(gameRecordRepository)
                    )
                    val resultsUiState by resultsViewModel.uiState.collectAsState()
                    val gameUiState by gameViewModel.uiState.collectAsState()
                    val sessionResult = gameUiState.sessionResult

                    LaunchedEffect(sessionResult) {
                        sessionResult?.let { resultsViewModel.loadForSession(it) }
                    }

                    ResultsScreen(
                        sessionResult = sessionResult,
                        resultsUiState = resultsUiState,
                        onPlayAgain = {
                            gameViewModel.restartGame()
                            navController.navigate(Routes.GAME) {
                                popUpTo(Routes.RESULTS) { inclusive = true }
                            }
                        },
                        onGoHome = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = showBottomBar,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    onReselect = { route ->
                        if (route == Routes.HISTORY) historyViewModel.onTabReselected()
                    },
                    onReselectLong = { route ->
                        if (route == Routes.HISTORY) historyViewModel.resetToTop()
                    }
                )
            }
        }
    }
}
