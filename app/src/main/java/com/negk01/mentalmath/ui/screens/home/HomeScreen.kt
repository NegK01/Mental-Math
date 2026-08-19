package com.negk01.mentalmath.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.ThemePreference
import com.negk01.mentalmath.presentation.home.HomeViewModel
import com.negk01.mentalmath.ui.screens.home.components.HomeHeader
import com.negk01.mentalmath.ui.screens.home.components.OnboardingDialog
import com.negk01.mentalmath.ui.screens.home.components.RecentScoresCard
import com.negk01.mentalmath.ui.screens.home.components.StartGameButton
import com.negk01.mentalmath.ui.screens.home.components.StreakCalendarDialog
import com.negk01.mentalmath.ui.theme.BottomNavContentPadding
import com.negk01.mentalmath.ui.theme.Spacing

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    selectedTheme: ThemePreference,
    selectedDifficulty: Difficulty,
    onThemeChange: (ThemePreference) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onStartGame: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    contentPadding = PaddingValues(start = Spacing.Lg, top = Spacing.Md, end = Spacing.Lg, bottom = Spacing.Xl),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Lg)
                ) {
                    item {
                        HomeHeader(
                            dailyStreak = uiState.dailyStreak,
                            onClick = viewModel::showCalendarDialog
                        )
                    }
                    item {
                        RecentScoresCard(records = uiState.recentRecords)
                    }
                }

                Box(
                    modifier = Modifier.padding(start = 18.dp, top = Spacing.Lg, end = 18.dp, bottom = BottomNavContentPadding)
                ) {
                    StartGameButton(onClick = onStartGame)
                }
            }
        }

        OnboardingDialog(
            visible = uiState.showOnboarding,
            selectedTheme = selectedTheme,
            selectedDifficulty = selectedDifficulty,
            onThemeChange = onThemeChange,
            onDifficultyChange = onDifficultyChange,
            onDismiss = viewModel::markOnboardingShown
        )

        if (uiState.showCalendarDialog) {
            StreakCalendarDialog(
                onDismiss = viewModel::hideCalendarDialog,
                streakStartDate = uiState.streakStartDate,
                streakEndDate = uiState.streakEndDate
            )
        }
    }
}
