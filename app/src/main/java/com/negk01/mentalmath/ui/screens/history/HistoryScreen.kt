package com.negk01.mentalmath.ui.screens.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.negk01.mentalmath.R
import com.negk01.mentalmath.presentation.history.HistoryViewModel
import com.negk01.mentalmath.ui.components.GameRecordItem
import com.negk01.mentalmath.ui.screens.history.components.EmptyState
import com.negk01.mentalmath.ui.screens.history.components.HistorySummaryCard
import com.negk01.mentalmath.ui.theme.BottomNavContentPadding
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.Spacing
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.isActive

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.scrollToTopEvent.collect {
            try {
                if (listState.firstVisibleItemIndex > 5) {
                    listState.scrollToItem(2)
                }
                listState.animateScrollToItem(0)
            } catch (e: CancellationException) {
                if (!coroutineContext.isActive) throw e
            }
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
                contentPadding = PaddingValues(start = Spacing.Lg, top = Spacing.Md, end = Spacing.Lg, bottom = BottomNavContentPadding),
                verticalArrangement = Arrangement.spacedBy(Spacing.Md)
            ) {
                item {
                    HistorySummaryCard(
                        totalGames = uiState.totalGames,
                        averageAccuracy = uiState.averageAccuracy,
                        averageTime = uiState.averageTimeSeconds,
                        bestEasy = uiState.bestEasyRecord,
                        bestMedium = uiState.bestMediumRecord,
                        bestHard = uiState.bestHardRecord,
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
                        EmptyState(
                            icon = Icons.Outlined.Inbox,
                            title = stringResource(R.string.history_empty),
                            message = stringResource(R.string.history_empty_hint)
                        )
                    }
                }

                uiState.displayRecords.forEachIndexed { index, record ->
                    item(key = record.playedAt) {
                        GameRecordItem(record = record)
                    }
                    if ((index + 1) % HistoryViewModel.PAGE_SIZE == 0 && index < uiState.displayRecords.lastIndex) {
                        item(key = "divider_${record.playedAt}") {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = Spacing.Xl),
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
                                .padding(vertical = Spacing.Sm),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                if (uiState.hasMore && uiState.totalGames > HistoryViewModel.PAGE_SIZE && !uiState.isLoadingMore) {
                    item {
                        OutlinedButton(
                            onClick = viewModel::loadMore,
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = Opacity.Scrim)),
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