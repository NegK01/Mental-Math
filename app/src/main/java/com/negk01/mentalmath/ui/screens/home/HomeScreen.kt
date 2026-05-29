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
import com.negk01.mentalmath.presentation.home.HomeViewModel
import com.negk01.mentalmath.ui.theme.BottomNavContentPadding
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.screens.home.components.HomeHeader
import com.negk01.mentalmath.ui.screens.home.components.RecentScoresCard
import com.negk01.mentalmath.ui.screens.home.components.StartGameButton

@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    viewModel: HomeViewModel
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
                        HomeHeader(dailyStreak = uiState.dailyStreak)
                    }
                    item {
                        RecentScoresCard(records = uiState.recentRecords)
                    }
                }

                Box(
                    modifier = Modifier.padding(start = 18.dp, top = 16.dp, end = 18.dp, bottom = BottomNavContentPadding)
                ) {
                    StartGameButton(onClick = onStartGame)
                }
            }
        }
    }
}
