//package com.negk01.mentalmath.ui.components
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.History
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.res.stringResource
//import androidx.navigation.NavController
//import com.negk01.mentalmath.R
//import com.negk01.mentalmath.navigation.Routes
//
//@Composable
//fun BottomNavBar(
//    navController: NavController,
//    currentRoute: String?
//) {
//    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.surface
//    ) {
//        NavigationBarItem(
//            selected = currentRoute == Routes.HOME,
//            onClick = { navController.navigateSingleTopTo(Routes.HOME, currentRoute) },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Home,
//                    contentDescription = stringResource(R.string.nav_home)
//                )
//            },
//            label = { Text(stringResource(R.string.nav_home)) },
//            colors = navigationColors()
//        )a
//
//        NavigationBarItem(
//            selected = currentRoute == Routes.HISTORY,
//            onClick = { navController.navigateSingleTopTo(Routes.HISTORY, currentRoute) },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.History,
//                    contentDescription = stringResource(R.string.nav_history)
//                )
//            },
//            label = { Text(stringResource(R.string.nav_history)) },
//            colors = navigationColors()
//        )
//
//        NavigationBarItem(
//            selected = currentRoute == Routes.CONFIG,
//            onClick = { navController.navigateSingleTopTo(Routes.CONFIG, currentRoute) },
//            icon = {
//                Icon(
//                    imageVector = Icons.Default.Settings,
//                    contentDescription = stringResource(R.string.nav_config)
//                )
//            },
//            label = { Text(stringResource(R.string.nav_config)) },
//            colors = navigationColors()
//        )
//    }
//}
//
//private fun NavController.navigateSingleTopTo(route: String, currentRoute: String?) {
//    if (currentRoute == route) return
//
//    navigate(route) {
//        launchSingleTop = true
//        restoreState = true
//        popUpTo(graph.startDestinationId) {
//            saveState = true
//        }
//    }
//}
//
//@Composable
//private fun navigationColors() = NavigationBarItemDefaults.colors(
//    selectedIconColor = MaterialTheme.colorScheme.primary,
//    selectedTextColor = MaterialTheme.colorScheme.primary,
//    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
//    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
//    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
//)

package com.negk01.mentalmath.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.negk01.mentalmath.R
import com.negk01.mentalmath.navigation.Routes

private data class NavItem(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector
)

private val navItems = listOf(
    NavItem(Routes.HOME, R.string.nav_home, Icons.Default.Home),
    NavItem(Routes.HISTORY, R.string.nav_history, Icons.Default.History),
    NavItem(Routes.CONFIG, R.string.nav_config, Icons.Default.Settings)
)

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?,
    // Callback genérico invocado cuando el usuario toca el item ya activo.
    // Default vacío — HomeScreen y ConfigScreen no necesitan reaccionar.
    onReselect: (route: String) -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute == item.route) {
                        onReselect(item.route)
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelRes)
                    )
                },
                label = {
                    Text(stringResource(item.labelRes))
                },
                colors = navigationColors() // 👈 ESTO ES LO QUE TE FALTABA
            )
        }
    }
}

@Composable
private fun navigationColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)