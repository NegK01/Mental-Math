package com.negk01.mentalmath.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.negk01.mentalmath.R
import com.negk01.mentalmath.presentation.history.HistoryViewModel
import com.negk01.mentalmath.ui.components.BottomNavBar
import com.negk01.mentalmath.ui.screens.history.components.HistorySummaryCard
import com.negk01.mentalmath.ui.screens.home.components.RecentScoreItem

@Composable
fun HistoryScreen(
    navController: NavController,
    currentRoute: String?,
    viewModel: HistoryViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HistorySummaryCard(
                        totalGames = uiState.totalGames,
                        averageAccuracy = uiState.averageAccuracy,
                        averageTime = uiState.averageTimeSeconds,
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.history_title),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (uiState.records.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.history_empty),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(uiState.records) { record ->
                        RecentScoreItem(record = record)
                    }
                }
            }
        }
    }
}
