package com.negk01.mentalmath.ui.screens.home

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
import com.negk01.mentalmath.presentation.home.HomeViewModel
import com.negk01.mentalmath.ui.components.BottomNavBar
import com.negk01.mentalmath.ui.screens.home.components.DailyStreakCard
import com.negk01.mentalmath.ui.screens.home.components.HomeHeader
import com.negk01.mentalmath.ui.screens.home.components.RecentScoresCard
import com.negk01.mentalmath.ui.screens.home.components.StartGameButton
import com.negk01.mentalmath.ui.theme.Background

@Composable
fun HomeScreen(
    navController: NavController,
    currentRoute: String?,
    viewModel: HomeViewModel = viewModel()
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
                    HomeHeader(
                        title = uiState.title,
                        subtitle = uiState.subtitle
                    )
                }

                item {
                    DailyStreakCard(
                        streak = uiState.dailyStreak
                    )
                }

                item {
                    RecentScoresCard(
                        scores = uiState.recentScores
                    )
                }

                item {
                    StartGameButton(
                        onClick = {
                            navController.navigate(Routes.GAME)
                        }
                    )
                }
            }
        }
    }
}