package com.negk01.mentalmath.ui.screens.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.negk01.mentalmath.ui.screens.config.components.*
import com.negk01.mentalmath.ui.screens.home.components.BottomNavBar
import com.negk01.mentalmath.ui.theme.Background
import com.negk01.mentalmath.ui.theme.TextPrimary

@Composable
fun ConfigScreen(navController: NavController,
                 currentRoute: String?) {

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    var selectedDifficulty by remember { mutableStateOf("Difícil") }
    var soundEnabled by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = Background,
        bottomBar = {
//            BottomNavBar(
//                selectedIndex = 2,
//                onItemSelected = {}
//            )
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                Text(
                    text = "Configuración",
//                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                DifficultySelector(
                    selected = selectedDifficulty,
                    onSelect = { selectedDifficulty = it }
                )
            }

            item {
                OptionSwitch(
                    title = "Efectos de sonido",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it }
                )
            }

            item {
                DangerZone(
                    onDelete = {
                        // luego conectamos con Room
                    }
                )
            }
        }
    }
}