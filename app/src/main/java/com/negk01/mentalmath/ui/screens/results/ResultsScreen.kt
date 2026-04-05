package com.negk01.mentalmath.ui.screens.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 12.dp
                    )
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.results_title),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 18.dp)
                        )
                    }

                    item {
                        ResultsSummaryCard(
                            correctAnswers = correctAnswers,
                            totalRounds = sessionResult?.totalRounds ?: 0,
                            averageTime = averageTime
                        )
                    }

                    // RoundDetailsCard como un único item del LazyColumn.
                    // La Card crece con su Column interno — sin scroll anidado,
                    // sin cálculos de altura. Máximo 12 items: Column es suficiente.
                    item {
                        RoundDetailsCard(
                            modifier = Modifier.padding(top = 18.dp),
                            items = roundDetails
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    thickness = 1.dp
                )

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
}