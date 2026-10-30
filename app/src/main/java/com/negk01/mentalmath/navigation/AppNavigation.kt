package com.negk01.mentalmath.navigation

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.negk01.mentalmath.R
import com.negk01.mentalmath.data.billing.BillingConstants
import com.negk01.mentalmath.data.billing.BillingEvent
import com.negk01.mentalmath.data.billing.BillingManager
import com.negk01.mentalmath.data.local.db.DatabaseProvider
import com.negk01.mentalmath.data.notification.NotificationHelper
import com.negk01.mentalmath.data.notification.StreakNotificationScheduler
import com.negk01.mentalmath.data.repository.GameRecordRepositoryImpl
import com.negk01.mentalmath.data.repository.SettingsRepositoryImpl
import com.negk01.mentalmath.domain.model.GameSound
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
import com.negk01.mentalmath.ui.audio.GameSoundPlayer
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
    val applicationContext = context.applicationContext
    val soundPlayer = remember(applicationContext) { GameSoundPlayer(applicationContext) }
    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    val database = remember(context) { DatabaseProvider.getDatabase(context) }
    val settingsRepository = remember(database) { SettingsRepositoryImpl(database.settingsDao()) }
    val gameRecordRepository = remember(database) { GameRecordRepositoryImpl(database.gameRecordDao()) }
    val billingManager = remember { BillingManager(context.applicationContext) }

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

    LaunchedEffect(gameViewModel) {
        gameViewModel.completedGameSaves.collect {
            NotificationHelper.cancelStreakNotification(context)
            if (configViewModel.uiState.value.notificationsEnabled) {
                StreakNotificationScheduler.replaceReminderAfterGame(context)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val activeRoute by rememberUpdatedState(currentRoute)
    var pendingToastMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                pendingToastMessage?.let { message ->
                    Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
                    pendingToastMessage = null
                }
            } else if (event == Lifecycle.Event.ON_PAUSE && activeRoute == Routes.GAME) {
                gameViewModel.pauseGame()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(billingManager) {
        billingManager.startConnection()
        onDispose {
            billingManager.destroy()
        }
    }

    val thankYouTemplate by rememberUpdatedState(stringResource(R.string.support_thank_you_toast))
    val thankYouGeneric by rememberUpdatedState(stringResource(R.string.support_thank_you_toast_generic))
    val pendingMessage by rememberUpdatedState(stringResource(R.string.support_pending))
    val tier1Label by rememberUpdatedState(stringResource(R.string.support_tier_1_label))
    val tier2Label by rememberUpdatedState(stringResource(R.string.support_tier_2_label))
    val tier3Label by rememberUpdatedState(stringResource(R.string.support_tier_3_label))

    LaunchedEffect(billingManager) {
        billingManager.billingEvents.collect { event ->
            when (event) {
                is BillingEvent.Success -> {
                    try {
                        settingsRepository.markHomeSupportCardDismissed()
                    } catch (e: Exception) {
                        Log.w("AppNavigation", "Failed to mark support card dismissed", e)
                    }
                    homeViewModel.onDonationCompleted()
                    configViewModel.onDonationCompleted()
                    val tierLabel = when (event.productId) {
                        BillingConstants.TIP_SMALL -> tier1Label
                        BillingConstants.TIP_MEDIUM -> tier2Label
                        BillingConstants.TIP_LARGE -> tier3Label
                        else -> ""
                    }
                    val message = if (tierLabel.isNotBlank()) {
                        val detail = if (event.quantity > 1) {
                            "${event.quantity}x $tierLabel"
                        } else {
                            tierLabel
                        }
                        thankYouTemplate.format(detail)
                    } else {
                        thankYouGeneric
                    }
                    if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
                    } else {
                        pendingToastMessage = message
                    }
                }
                BillingEvent.Pending -> {
                    Toast.makeText(context.applicationContext, pendingMessage, Toast.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }
    }

    val configUiState by configViewModel.uiState.collectAsState()

    DisposableEffect(lifecycleOwner, soundPlayer) {
        soundPlayer.setForeground(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> soundPlayer.setForeground(true)
                Lifecycle.Event.ON_PAUSE -> soundPlayer.setForeground(false)
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            soundPlayer.setForeground(false)
        }
    }

    DisposableEffect(soundPlayer) {
        onDispose { soundPlayer.release() }
    }

    LaunchedEffect(gameViewModel, soundPlayer) {
        gameViewModel.soundEvents.collect { sound ->
            if (activeRoute == Routes.GAME && !gameViewModel.uiState.value.isPaused) {
                soundPlayer.play(sound)
            }
        }
    }

    LaunchedEffect(gameViewModel.uiState, soundPlayer) {
        gameViewModel.uiState.collect { state ->
            if (state.isPaused) soundPlayer.stop()
        }
    }

    SideEffect {
        soundPlayer.setEnabled(configUiState.isSettingsLoaded && configUiState.soundEnabled)
    }

    LaunchedEffect(currentRoute, soundPlayer) {
        soundPlayer.stop()
    }

    LaunchedEffect(configUiState.languagePreference) {
        val targetLocales = configUiState.languagePreference.toLocaleListCompat()
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (targetLocales != currentLocales) {
            AppCompatDelegate.setApplicationLocales(targetLocales)
        }
    }

    LaunchedEffect(configUiState.isSettingsLoaded, configUiState.notificationsEnabled) {
        if (!configUiState.isSettingsLoaded) return@LaunchedEffect
        if (configUiState.notificationsEnabled) {
            StreakNotificationScheduler.keepOrScheduleNextReminder(context)
        } else {
            StreakNotificationScheduler.cancelReminderChain(context)
        }
    }

    MentalMathTheme(themePreference = configUiState.themePreference) {
        val showBottomBar = currentRoute in listOf(Routes.HOME, Routes.HISTORY, Routes.CONFIG)

        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                enterTransition = { fadeIn(animationSpec = tween(1)) },
                exitTransition = { fadeOut(animationSpec = tween(1)) },
                popEnterTransition = { fadeIn(animationSpec = tween(1)) },
                popExitTransition = { fadeOut(animationSpec = tween(1)) },
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Routes.HOME) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        billingManager = billingManager,
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
                        viewModel = configViewModel,
                        billingManager = billingManager
                    )
                }

                composable(Routes.GAME) {
                    val gameUiState by gameViewModel.uiState.collectAsState()
                    LaunchedEffect(gameUiState.hasSession) {
                        if (!gameUiState.hasSession) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.id) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                    if (gameUiState.hasSession) GameScreen(
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

                composable(Routes.RESULTS) { resultsEntry ->
                    val resultsViewModel: ResultsViewModel = viewModel(
                        factory = ResultsViewModelFactory(gameRecordRepository)
                    )
                    val resultsUiState by resultsViewModel.uiState.collectAsState()
                    val gameUiState by gameViewModel.uiState.collectAsState()
                    val sessionResult = remember(resultsEntry) {
                        gameViewModel.uiState.value.sessionResult
                    }

                    LaunchedEffect(resultsEntry) {
                        if (sessionResult == null) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.id) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                    LaunchedEffect(sessionResult, gameUiState.savedRecordId) {
                        if (sessionResult != null) {
                            resultsViewModel.loadForSession(sessionResult, gameUiState.savedRecordId)
                        }
                    }
                    LaunchedEffect(resultsViewModel, resultsEntry, soundPlayer) {
                        resultsEntry.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            resultsViewModel.uiState.collect {
                                if (resultsViewModel.consumeRecordCelebration()) {
                                    soundPlayer.play(GameSound.PERSONAL_BEST)
                                }
                            }
                        }
                    }
                    if (sessionResult != null) ResultsScreen(
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
