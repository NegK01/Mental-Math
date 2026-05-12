package com.negk01.mentalmath.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.presentation.history.HistoryViewModel
import com.negk01.mentalmath.ui.screens.history.components.HistorySummaryCard
import com.negk01.mentalmath.ui.components.GameRecordItem

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val shouldScrollToTop by viewModel.shouldScrollToTop.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(shouldScrollToTop) {
        if (shouldScrollToTop) {
            listState.animateScrollToItem(0)
            viewModel.consumeScrollToTop()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding(),
                contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (uiState.displayRecords.isEmpty() && !uiState.isLoadingMore) {
                    item {
                        Text(
                            text = stringResource(R.string.history_empty),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                uiState.displayRecords.forEachIndexed { index, record ->
                    item(key = record.playedAt) {
                        Box(modifier = Modifier.animateItem()) {
                            GameRecordItem(record = record)
                        }
                    }
                    if ((index + 1) % 5 == 0 && index < uiState.displayRecords.lastIndex) {
                        item(key = "divider_${record.playedAt}") {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                thickness = 1.dp
                            )
                        }
                    }
                }

                if (uiState.isLoadingMore) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                if (uiState.hasMore && uiState.totalGames > 5 && !uiState.isLoadingMore) {
                    item {
                        OutlinedButton(
                            onClick = viewModel::loadMore,
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(stringResource(R.string.history_load_more))
                        }
                    }
                }
            }
        }
    }
}