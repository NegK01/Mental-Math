package com.negk01.mentalmath.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.negk01.mentalmath.R
import com.negk01.mentalmath.navigation.Routes
import com.negk01.mentalmath.ui.theme.Motion
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing

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
    onReselect: (route: String) -> Unit = {},
    onReselectLong: (route: String) -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Lg, vertical = Spacing.Xl),
        shape = RoundedCornerShape(Radius.NavBar),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Lg, vertical = Spacing.Md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val haptic = LocalHapticFeedback.current

            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                val primary = MaterialTheme.colorScheme.primary
                val background = animateColorAsState(
                    targetValue = if (isSelected) primary.copy(alpha = Opacity.BadgeContainer) else primary.copy(alpha = 0f),
                    animationSpec = tween(Motion.Slow, easing = FastOutSlowInEasing),
                    label = "backgroundColor"
                ).value
                val contentColor = animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(Motion.Slow, easing = FastOutSlowInEasing),
                    label = "contentColor"
                ).value

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.Xl))
                        .background(background)
                        .combinedClickable(
                            onClick = {
                                if (isSelected) {
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
                            onLongClick = {
                                if (isSelected) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onReselectLong(item.route)
                                }
                            }
                        )
                        .padding(horizontal = Spacing.Lg, vertical = Spacing.Sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelRes),
                        tint = contentColor
                    )

                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(animationSpec = tween(Motion.Slow)) + expandHorizontally(animationSpec = tween(Motion.Slow, easing = FastOutSlowInEasing), expandFrom = Alignment.Start),
                        exit = fadeOut(animationSpec = tween(Motion.Medium)) + shrinkHorizontally(animationSpec = tween(Motion.Medium, easing = FastOutSlowInEasing), shrinkTowards = Alignment.Start)
                    ) {
                        Text(
                            text = stringResource(item.labelRes),
                            color = contentColor,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = Spacing.Sm)
                        )
                    }
                }
            }
        }
    }
}
