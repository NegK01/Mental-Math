package com.negk01.mentalmath.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.presentation.game.GameViewModel
import com.negk01.mentalmath.ui.screens.game.components.AnswerDisplay
import com.negk01.mentalmath.ui.screens.game.components.GameProgressCard
import com.negk01.mentalmath.ui.screens.game.components.GameTopBar
import com.negk01.mentalmath.ui.screens.game.components.MathQuestionCard
import com.negk01.mentalmath.ui.screens.game.components.NumberPad
import com.negk01.mentalmath.ui.screens.game.components.PauseDialog
import com.negk01.mentalmath.ui.theme.Background
import com.negk01.mentalmath.ui.utils.toDisplayName
import androidx.compose.runtime.LaunchedEffect

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            navController.navigate(Routes.RESULTS) {
                popUpTo(Routes.GAME) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            GameTopBar(
                onPauseClick = viewModel::pauseGame,
                onExitClick = {
                    val shouldShowResults = viewModel.abandonGame()

                    if (shouldShowResults) {
                        navController.navigate(Routes.RESULTS)
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    GameProgressCard(
                        difficulty = uiState.difficulty.toDisplayName(),
                        currentRound = uiState.currentRound,
                        totalRounds = uiState.totalRounds,
                        timeLeftSeconds = uiState.timeLeftSeconds
                    )
                }

                item {
                    MathQuestionCard(
                        questionText = uiState.questionText
                    )
                }

                item {
                    AnswerDisplay(
                        value = uiState.answerInput
                    )
                }

                item {
                    NumberPad(
                        onDigitClick = viewModel::onDigitPressed,
                        onClearClick = viewModel::onClearDigit,
                        onSubmitClick = viewModel::onSubmitAnswer,
                        isSubmitEnabled = uiState.answerInput.isNotBlank()
                    )
                }
            }

            if (uiState.isPaused) {
                PauseDialog(
                    onResume = viewModel::resumeGame
                )
            }
        }
    }
}