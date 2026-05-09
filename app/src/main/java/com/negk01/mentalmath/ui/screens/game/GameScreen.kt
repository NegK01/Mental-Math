package com.negk01.mentalmath.ui.screens.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = viewModel(),
    onGameAbandoned: () -> Unit = {}
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
                        onGameAbandoned()
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val buttonHeight = (maxHeight * 0.085f).coerceIn(44.dp, 66.dp)
                val questionFontSize = (maxHeight.value * 0.048f).coerceIn(26f, 42f).sp
                val sectionSpacing = (maxHeight * 0.015f).coerceIn(6.dp, 14.dp)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp)
                ) {
                    GameProgressCard(
                        currentRound = uiState.currentRound,
                        totalRounds = uiState.totalRounds,
                        timeLeftSeconds = uiState.timeLeftSeconds
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    MathQuestionCard(
                        questionText = uiState.questionText,
                        questionFontSize = questionFontSize
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // lastAnswerCorrect viene de GameUiState — activa el flash de color
                    AnswerDisplay(
                        value = uiState.answerInput,
                        lastAnswerCorrect = uiState.lastAnswerCorrect
                    )

                    Spacer(modifier = Modifier.height(sectionSpacing))

                    NumberPad(
                        onDigitClick = viewModel::onDigitPressed,
                        onClearClick = viewModel::onClearDigit,
                        onSubmitClick = viewModel::onSubmitAnswer,
                        isSubmitEnabled = uiState.answerInput.isNotBlank() && !uiState.isInputLocked,
                        buttonHeight = buttonHeight
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.isPaused,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PauseDialog(onResume = viewModel::resumeGame)
            }
        }
    }
}
