package com.negk01.mentalmath.ui.screens.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.ui.theme.Spacing
import androidx.navigation.NavController
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.RoundDetail
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.presentation.results.DeltaState
import com.negk01.mentalmath.presentation.results.ResultsUiState
import com.negk01.mentalmath.ui.screens.results.components.OperatorInsightRow
import com.negk01.mentalmath.ui.screens.results.components.ResultsActions
import com.negk01.mentalmath.ui.screens.results.components.ResultsMomentumSection
import com.negk01.mentalmath.ui.screens.results.components.RoundDetailsSection
import com.negk01.mentalmath.ui.screens.results.components.ScoreHeroSection

@Composable
fun ResultsScreen(
    navController: NavController,
    sessionResult: GameSessionResult?,
    resultsUiState: ResultsUiState,
    onPlayAgain: () -> Unit
) {
    val roundDetails = sessionResult?.roundResults?.map { round ->
        RoundDetail(
            expression = stringResource(R.string.results_round_expression, round.question, round.correctAnswer),
            userAnswer = round.userAnswer.toString(),
            isCorrect = round.isCorrect,
            time = stringResource(R.string.common_seconds_int, round.timeSpentSeconds)
        )
    }.orEmpty()

    val correctAnswers = sessionResult?.correctAnswers ?: 0
    val totalRounds = sessionResult?.totalRounds ?: 0

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = Spacing.Lg, end = Spacing.Lg, top = Spacing.Xl, bottom = Spacing.Md)
            ) {
                item {
                    ScoreHeroSection(
                        correctAnswers = correctAnswers,
                        totalRounds = totalRounds,
                        difficulty = sessionResult?.difficulty,
                        completionStatus = sessionResult?.completionStatus ?: CompletionStatus.COMPLETED,
                        nextGoal = resultsUiState.nextGoal,
                        isNewRecord = resultsUiState.delta is DeltaState.NewRecord,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

                item {
                    ResultsMomentumSection(
                        maxStreak = resultsUiState.maxStreak,
                        delta = resultsUiState.delta,
                        modifier = Modifier.padding(bottom = Spacing.Sm)
                    )
                }

                resultsUiState.operatorInsight?.let { insight ->
                    item {
                        OperatorInsightRow(
                            insight = insight,
                            modifier = Modifier.padding(bottom = 18.dp)
                        )
                    }
                }

                item {
                    RoundDetailsSection(
                        modifier = Modifier.padding(top = if (resultsUiState.operatorInsight == null) 18.dp else 0.dp),
                        items = roundDetails
                    )
                }
            }

            ResultsActions(
                modifier = Modifier.padding(horizontal = Spacing.Lg, vertical = Spacing.Md),
                onGoHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onPlayAgain = onPlayAgain
            )
        }
    }
}
