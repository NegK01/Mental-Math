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
import androidx.navigation.NavController
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.RoundDetail
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.ui.screens.results.components.ResultsActions
import com.negk01.mentalmath.ui.screens.results.components.ResultsSummarySection
import com.negk01.mentalmath.ui.screens.results.components.RoundDetailsSection
import com.negk01.mentalmath.ui.screens.results.components.ScoreHeroSection

@Composable
fun ResultsScreen(
    navController: NavController,
    currentRoute: String?,
    sessionResult: GameSessionResult?,
    onPlayAgain: () -> Unit
) {
    val roundDetails = sessionResult?.roundResults?.map { round ->
        RoundDetail(
            expression = stringResource(
                R.string.results_round_expression,
                round.question,
                round.correctAnswer
            ),
            userAnswer = round.userAnswer.toString(),
            isCorrect = round.isCorrect,
            time = stringResource(R.string.common_seconds_int, round.timeSpentSeconds)
        )
    }.orEmpty()

    val correctAnswers = sessionResult?.correctAnswers ?: 0
    val totalRounds = sessionResult?.totalRounds ?: 0
    val averageTime = sessionResult?.averageResponseTimeSeconds ?: 0.0

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
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp,
                    bottom = 12.dp
                )
            ) {
                item {
                    ScoreHeroSection(
                        correctAnswers = correctAnswers,
                        totalRounds = totalRounds,
                        difficulty = sessionResult?.difficulty,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                item {
                    ResultsSummarySection(
                        correctAnswers = correctAnswers,
                        averageTime = averageTime
                    )
                }

                item {
                    RoundDetailsSection(
                        modifier = Modifier.padding(top = 18.dp),
                        items = roundDetails
                    )
                }
            }

            ResultsActions(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
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
