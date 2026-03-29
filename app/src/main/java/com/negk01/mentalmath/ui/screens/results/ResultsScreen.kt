package com.negk01.mentalmath.ui.screens.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.RoundDetail
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.ui.screens.results.components.ResultsActions
import com.negk01.mentalmath.ui.screens.results.components.ResultsSummaryCard
import com.negk01.mentalmath.ui.screens.results.components.RoundDetailsCard

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
                round.userAnswer
            ),
            userAnswer = round.userAnswer.toString(),
            isCorrect = round.isCorrect,
            time = stringResource(R.string.common_seconds_int, round.timeSpentSeconds)
        )
    }.orEmpty()

    val correctAnswers = sessionResult?.correctAnswers ?: 0
    val averageTime = sessionResult?.averageResponseTimeSeconds ?: 0.0

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.results_title),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                ResultsSummaryCard(
                    correctAnswers = correctAnswers,
                    totalRounds = sessionResult?.totalRounds ?: 0,
                    averageTime = averageTime
                )

                RoundDetailsCard(
                    modifier = Modifier.weight(1f),
                    items = roundDetails
                )

                ResultsActions(
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
}