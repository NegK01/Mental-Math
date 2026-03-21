package com.negk01.mentalmath.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.navigation.NavController
import com.negk01.mentalmath.domain.model.Score
import com.negk01.mentalmath.ui.screens.home.components.BottomNavBar
import com.negk01.mentalmath.ui.screens.home.components.HomeHeader
import com.negk01.mentalmath.ui.screens.home.components.RecentScoresCard
import com.negk01.mentalmath.ui.screens.home.components.StartGameButton
import com.negk01.mentalmath.ui.theme.Background

@Composable
fun HomeScreen(navController: NavController,
               currentRoute: String?) {

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val recentScores = remember {
        listOf(
            Score(
                difficulty = "Hard",
                date = "18/3/2026",
                result = "3/3",
                time = "0.0s"
            ),
            Score(
                difficulty = "Hard",
                date = "18/3/2026",
                result = "4/5",
                time = "3.1s"
            )
        )
    }

    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Scaffold(
            containerColor = Background,
            bottomBar = {
//                BottomNavBar(
//                    selectedIndex = 0,
//                    onItemSelected = {}
//                )
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    HomeHeader(
                        title = "Mental Math",
                        subtitle = "Entrena tu agilidad mental"
                    )
                }

                item {
                    StartGameButton(
                        onClick = {}
                    )
                }

                item {
                    RecentScoresCard(
                        scores = recentScores
                    )
                }
            }
        }
    }
}