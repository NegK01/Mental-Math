package com.negk01.mentalmath.ui.screens.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.negk01.mentalmath.presentation.results.ResultsViewModel
import com.negk01.mentalmath.ui.screens.home.components.BottomNavBar
import com.negk01.mentalmath.ui.screens.results.components.ResultsSummaryCard
import com.negk01.mentalmath.ui.screens.results.components.RoundDetailsCard
import com.negk01.mentalmath.ui.theme.Background
import com.negk01.mentalmath.ui.theme.TextPrimary

@Composable
fun ResultsScreen(
    navController: NavController,
    currentRoute: String?,
    viewModel: ResultsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute
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
                    Text(
                        text = "Resultados",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                item {
                    ResultsSummaryCard(
                        correctAnswers = uiState.correctAnswers,
                        averageTime = uiState.averageTime
                    )
                }

                item {
                    RoundDetailsCard(
                        items = uiState.roundDetails
                    )
                }
            }
        }
    }
}