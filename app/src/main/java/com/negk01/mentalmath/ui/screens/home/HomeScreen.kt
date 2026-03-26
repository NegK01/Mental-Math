package com.negk01.mentalmath.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavController
import com.negk01.mentalmath.presentation.home.HomeViewModel
import com.negk01.mentalmath.ui.components.BottomNavBar
import com.negk01.mentalmath.ui.screens.home.components.HomeHeader
import com.negk01.mentalmath.ui.screens.home.components.RecentScoresCard
import com.negk01.mentalmath.ui.screens.home.components.StartGameButton

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    currentRoute: String?,
    onStartGame: () -> Unit,
    viewModel: HomeViewModel
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
            ) {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        HomeHeader(
                            dailyStreak = uiState.dailyStreak
                        )
                    }

                    item {
                        RecentScoresCard(
                            records = uiState.recentRecords
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    StartGameButton(
                        onClick = onStartGame
                    )
                }
            }
        }
    }
}
