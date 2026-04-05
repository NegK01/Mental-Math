package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.RoundDetail

// Column en lugar de LazyColumn — con máximo 12 items no se necesita
// virtualización. La Card crece con el contenido sin restricción de altura
// y sin scroll anidado.
@Composable
fun RoundDetailsCard(
    items: List<RoundDetail>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.results_round_details_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (items.isEmpty()) {
                Text(
                    text = stringResource(R.string.history_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                items.forEachIndexed { index, item ->
                    RoundDetailItem(item = item)

                    // Divider entre items — ayuda al ojo a separar filas
                    // en la lista. No se pone después del último item.
                    if (index < items.lastIndex) {
//                        HorizontalDivider(
//                            modifier = Modifier.padding(vertical = 4.dp),
//                            color = MaterialTheme.colorScheme.surfaceVariant,
//                            thickness = 1.dp
//                        )

                        Spacer(modifier = Modifier.size(6.dp))

                    }
                }
            }
        }
    }
}