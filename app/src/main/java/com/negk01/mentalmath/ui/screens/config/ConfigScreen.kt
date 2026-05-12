package com.negk01.mentalmath.ui.screens.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.negk01.mentalmath.R
import com.negk01.mentalmath.presentation.config.ConfigViewModel
import com.negk01.mentalmath.ui.screens.config.components.DangerZone
import com.negk01.mentalmath.ui.screens.config.components.DifficultySelector
import com.negk01.mentalmath.ui.screens.config.components.LanguagePreferenceSelector
import com.negk01.mentalmath.ui.screens.config.components.ThemePreferenceSelector

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
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
                contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(26.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.config_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    LanguagePreferenceSelector(
                        selected = uiState.languagePreference,
                        onSelect = viewModel::onLanguagePreferenceSelected
                    )
                }

                item {
                    DifficultySelector(
                        selected = uiState.selectedDifficulty,
                        onSelect = viewModel::onDifficultySelected
                    )
                }

                // DEV: Feature de sonido deshabilitada. Descomentar y conectar cuando se implemente el audio en GameViewModel. Requiere: SoundManager, lógica en GameViewModel. SettingsRepository ya tiene soundEnabled.
                // item {
                //     OptionSwitch(
                //         title = stringResource(R.string.config_sound_title),
                //         description = stringResource(R.string.config_sound_description),
                //         checked = uiState.soundEnabled,
                //         onCheckedChange = viewModel::onSoundEnabledChanged
                //     )
                // }

                item {
                    ThemePreferenceSelector(
                        selected = uiState.themePreference,
                        onSelect = viewModel::onThemePreferenceSelected
                    )
                }

                item {
                    DangerZone(
                        buttonText = stringResource(R.string.config_delete_history_button),
                        onDelete = viewModel::showDeleteHistoryDialog
                    )
                }
            }

            if (uiState.showDeleteHistoryDialog) {
                AlertDialog(
                    onDismissRequest = viewModel::hideDeleteHistoryDialog,
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    title = {
                        Text(text = stringResource(R.string.config_delete_dialog_title))
                    },
                    text = {
                        Text(text = stringResource(R.string.config_delete_dialog_message))
                    },
                    confirmButton = {
                        TextButton(onClick = viewModel::clearScoresHistory) {
                            Text(
                                text = stringResource(R.string.config_delete_dialog_confirm),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideDeleteHistoryDialog) {
                            Text(text = stringResource(R.string.config_delete_dialog_cancel))
                        }
                    }
                )
            }
        }
    }
}
