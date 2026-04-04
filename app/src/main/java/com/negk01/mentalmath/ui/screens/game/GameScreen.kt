package com.negk01.mentalmath.ui.screens.game

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
import androidx.compose.ui.res.stringResource
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // ── Valores adaptativos derivados de la altura disponible ──────────
            // buttonHeight: 8.5% de la pantalla, entre 44dp (mínimo legible) y 66dp (máximo cómodo)
            val buttonHeight = (maxHeight * 0.085f).coerceIn(44.dp, 66.dp)

            // questionFontSize: 4.8% de la altura, entre 26sp y 42sp
            val questionFontSize = (maxHeight.value * 0.048f).coerceIn(26f, 42f).sp

            // Espaciado vertical entre secciones: 1.5% de la pantalla, entre 6dp y 14dp
            val sectionSpacing = (maxHeight * 0.015f).coerceIn(6.dp, 14.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp)
            ) {
                // ── Sección de progreso (flat, sin Card) ──────────────────────
                GameProgressCard(
                    currentRound = uiState.currentRound,
                    totalRounds = uiState.totalRounds,
                    timeLeftSeconds = uiState.timeLeftSeconds
                )

                // Espacio flexible superior — empuja el contenido hacia el centro
                Spacer(modifier = Modifier.weight(1f))

                // ── Pregunta matemática ───────────────────────────────────────
                MathQuestionCard(
                    questionText = uiState.questionText,
                    questionFontSize = questionFontSize
                )

                // Espacio flexible entre pregunta y respuesta (60% del superior)
                Spacer(modifier = Modifier.weight(0.6f))

                // ── Display de respuesta ──────────────────────────────────────
                AnswerDisplay(
                    value = uiState.answerInput
                )

                Spacer(modifier = Modifier.height(sectionSpacing))

                // ── Teclado numérico ──────────────────────────────────────────
                NumberPad(
                    onDigitClick = viewModel::onDigitPressed,
                    onClearClick = viewModel::onClearDigit,
                    onSubmitClick = viewModel::onSubmitAnswer,
                    isSubmitEnabled = uiState.answerInput.isNotBlank(),
                    buttonHeight = buttonHeight
                )
            }

            // ── Diálogo de pausa (overlay) ────────────────────────────────────
            if (uiState.isPaused) {
                PauseDialog(onResume = viewModel::resumeGame)
            }
        }
    }
}