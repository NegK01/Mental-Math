package com.negk01.mentalmath.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing

@Composable
fun <T> OptionSelector(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    optionLabelRes: (T) -> Int,
    modifier: Modifier = Modifier,
    title: String? = null,
    optionShape: Shape = RoundedCornerShape(Radius.Card),
    optionTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    optionFontWeight: FontWeight? = FontWeight.SemiBold,
    optionSpacing: Dp = Spacing.Sm,
    optionPadding: PaddingValues = PaddingValues(vertical = Spacing.Sm, horizontal = Spacing.Sm)
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Md)
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(optionSpacing)
        ) {
            options.forEach { option ->
                Surface(
                    onClick = { onSelect(option) },
                    modifier = Modifier.weight(1f),
                    shape = optionShape,
                    color = if (selected == option) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = stringResource(optionLabelRes(option)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(optionPadding),
                        style = optionTextStyle,
                        fontWeight = optionFontWeight,
                        textAlign = TextAlign.Center,
                        color = if (selected == option) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}
