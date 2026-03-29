package com.negk01.mentalmath.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.negk01.mentalmath.ui.utils.toLabelResId

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
        containerColor = MaterialTheme.colorScheme.background,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp, 8.dp, 16.dp, 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GameProgressCard(
                        difficulty = stringResource(uiState.difficulty.toLabelResId()),
                        currentRound = uiState.currentRound,
                        totalRounds = uiState.totalRounds,
                        timeLeftSeconds = uiState.timeLeftSeconds
                    )

                    MathQuestionCard(
                        questionText = uiState.questionText
                    )

                    AnswerDisplay(
                        value = uiState.answerInput
                    )
                }

                NumberPad(
                    onDigitClick = viewModel::onDigitPressed,
                    onClearClick = viewModel::onClearDigit,
                    onSubmitClick = viewModel::onSubmitAnswer,
                    isSubmitEnabled = uiState.answerInput.isNotBlank()
                )
            }

            if (uiState.isPaused) {
                PauseDialog(
                    onResume = viewModel::resumeGame
                )
            }
        }
    }
}
