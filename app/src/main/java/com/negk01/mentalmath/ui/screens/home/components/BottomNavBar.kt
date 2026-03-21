package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.ui.theme.Primary
import com.negk01.mentalmath.ui.theme.PrimaryContainer
import com.negk01.mentalmath.ui.theme.TextMuted

//@Composable
//fun BottomNavBar(
//    selectedIndex: Int,
//    onItemSelected: (Int) -> Unit
//) {
//    NavigationBar(
//        modifier = Modifier.navigationBarsPadding(),
//        containerColor = Color.White,
//        tonalElevation = 8.dp
//    ) {
//        NavigationBarItem(
//            selected = selectedIndex == 0,
//            onClick = { onItemSelected(0) },
//            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
//            label = { Text("Inicio") },
//            colors = navigationColors()
//        )
//
//        NavigationBarItem(
//            selected = selectedIndex == 1,
//            onClick = { onItemSelected(1) },
//            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Resultados") },
//            label = { Text("Resultados") },
//            colors = navigationColors()
//        )
//
//        NavigationBarItem(
//            selected = selectedIndex == 2,
//            onClick = { onItemSelected(2) },
//            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
//            label = { Text("Configuración") },
//            colors = navigationColors()
//        )
//    }
//}

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar(
//        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.White,
//        tonalElevation = 8.dp
    ) {

        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = { navController.navigateSingleTopTo(Routes.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            colors = navigationColors()
        )

        NavigationBarItem(
            selected = currentRoute == Routes.RESULTS,
            onClick = { navController.navigateSingleTopTo(Routes.RESULTS) },
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) },
            label = { Text("Resultados") },
            colors = navigationColors()
        )

        NavigationBarItem(
            selected = currentRoute == Routes.CONFIG,
            onClick = { navController.navigateSingleTopTo(Routes.CONFIG) },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Configuración") },
            colors = navigationColors()
        )
    }
}

private fun NavController.navigateSingleTopTo(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}

@Composable
private fun navigationColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Primary,
    selectedTextColor = Primary,
    indicatorColor = PrimaryContainer,
    unselectedIconColor = TextMuted,
    unselectedTextColor = TextMuted
)