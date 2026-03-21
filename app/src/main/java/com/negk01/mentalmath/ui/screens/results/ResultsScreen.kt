package com.negk01.mentalmath.ui.screens.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.RoundDetail
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.ui.screens.results.components.CompletionStatusBadge
import com.negk01.mentalmath.ui.screens.results.components.ResultsActions
import com.negk01.mentalmath.ui.screens.results.components.ResultsSummaryCard
import com.negk01.mentalmath.ui.screens.results.components.RoundDetailsCard
import com.negk01.mentalmath.ui.theme.Background
import com.negk01.mentalmath.ui.theme.TextPrimary

@Composable
fun ResultsScreen(
    navController: NavController,
    currentRoute: String?,
    sessionResult: GameSessionResult?,
    onPlayAgain: () -> Unit
) {
    val roundDetails = sessionResult?.roundResults?.map { round ->
        RoundDetail(
            expression = "${round.question} → ${round.userAnswer}",
            userAnswer = round.userAnswer.toString(),
            isCorrect = round.isCorrect,
            time = "${round.timeSpentSeconds}s"
        )
    }.orEmpty()

    val correctAnswers = sessionResult?.correctAnswers ?: 0
    val averageTime = sessionResult?.averageResponseTimeSeconds?.let {
        String.format("%.1fs", it)
    } ?: "--"

    Scaffold(
        containerColor = Background
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(18.dp),
//                ) {
                    Text(
                        text = "Resultados",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )


//                    if (sessionResult != null) {
//                        CompletionStatusBadge(
//                            status = sessionResult.completionStatus
//                        )
//                    }
//                }

                ResultsSummaryCard(
                    correctAnswers = correctAnswers,
                    averageTime = averageTime
                )

                RoundDetailsCard(
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