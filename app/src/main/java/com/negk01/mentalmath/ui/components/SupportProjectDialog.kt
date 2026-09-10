package com.negk01.mentalmath.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.data.billing.BillingConstants
import com.negk01.mentalmath.data.billing.BillingEvent
import com.negk01.mentalmath.data.billing.BillingManager
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.findActivity

private data class SupportTier(
    val productId: String,
    val price: String,
    val label: String
)

@Composable
fun SupportProjectDialog(
    billingManager: BillingManager,
    onDismiss: () -> Unit,
    onPurchaseSuccess: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val localizedPrices by billingManager.localizedPrices.collectAsState()
    val unavailableMessage = stringResource(R.string.support_unavailable)
    val errorMessage = stringResource(R.string.support_error)

    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(billingManager) {
        billingManager.billingEvents.collect { event ->
            isSubmitting = false
            when (event) {
                is BillingEvent.Success -> {
                    onPurchaseSuccess?.invoke()
                    onDismiss()
                }
                BillingEvent.Pending -> {
                    onDismiss()
                }
                is BillingEvent.Error -> {
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                BillingEvent.Unavailable -> {
                    Toast.makeText(
                        context,
                        unavailableMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                BillingEvent.Canceled -> {}
            }
        }
    }

    fun onSelectTier(productId: String) {
        val activity = context.findActivity()
        if (activity != null) {
            billingManager.launchPurchase(activity, productId)
        } else {
            isSubmitting = false
            Toast.makeText(
                context,
                unavailableMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val pricePlaceholder = stringResource(R.string.support_price_placeholder)
    val tier1Raw = localizedPrices[BillingConstants.TIP_SMALL] ?: pricePlaceholder
    val tier2Raw = localizedPrices[BillingConstants.TIP_MEDIUM] ?: pricePlaceholder
    val tier3Raw = localizedPrices[BillingConstants.TIP_LARGE] ?: pricePlaceholder

    val tiers = listOf(
        SupportTier(BillingConstants.TIP_SMALL, formatSupportPrice(tier1Raw), stringResource(R.string.support_tier_1_label)),
        SupportTier(BillingConstants.TIP_MEDIUM, formatSupportPrice(tier2Raw), stringResource(R.string.support_tier_2_label)),
        SupportTier(BillingConstants.TIP_LARGE, formatSupportPrice(tier3Raw), stringResource(R.string.support_tier_3_label))
    )
    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }

    AppDialog(
        onDismiss = onDismiss,
        dismissOnScrimTap = true,
        scrollable = true,
        horizontalMargin = Spacing.Sm,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(Spacing.Md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Sm)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Spacing.Xl)
            )
            Text(
                text = stringResource(R.string.support_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource(R.string.support_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
        )

        Spacer(modifier = Modifier.height(Spacing.Xs))

        SupportTiersGrid(
            tiers = tiers,
            selectedIndex = selectedIndex,
            onSelectIndex = { selectedIndex = it }
        )

        Spacer(modifier = Modifier.height(Spacing.Xs))

        Button(
            onClick = {
                isSubmitting = true
                val tier = tiers[selectedIndex]
                onSelectTier(tier.productId)
            },
            enabled = !isSubmitting && localizedPrices.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Radius.Xl),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.support_process_payment),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SupportTiersGrid(
    tiers: List<SupportTier>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val fontScale = density.fontScale

    val titleStyle = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
    val priceStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val totalWidthPx = constraints.maxWidth
        val spacingPx = with(density) { 6.dp.toPx() } * (tiers.size - 1).coerceAtLeast(0)
        val cardHorizontalPaddingPx = with(density) { (Spacing.Xs * 2).toPx() }
        val cardWidthPx = if (tiers.isNotEmpty()) ((totalWidthPx - spacingPx) / tiers.size).toInt() else 0
        val textWidthPx = (cardWidthPx - cardHorizontalPaddingPx.toInt()).coerceAtLeast(1)

        val priceFontSize = remember(tiers, textWidthPx, fontScale, priceStyle) {
            calculateFittingFontSize(
                texts = tiers.map { it.price },
                style = priceStyle,
                maxSize = 15.sp,
                minSize = 6.sp,
                maxWidthPx = textWidthPx,
                textMeasurer = textMeasurer
            )
        }

        val titleFontSize = remember(tiers, textWidthPx, fontScale, priceFontSize, titleStyle) {
            val targetTitleSize = (priceFontSize.value - 1.5f).coerceAtMost(13f).sp
            calculateFittingFontSize(
                texts = tiers.map { it.label },
                style = titleStyle,
                maxSize = targetTitleSize,
                minSize = 6.sp,
                maxWidthPx = textWidthPx,
                textMeasurer = textMeasurer
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tiers.forEachIndexed { index, tier ->
                SupportTierCard(
                    price = tier.price,
                    label = tier.label,
                    titleFontSize = titleFontSize,
                    priceFontSize = priceFontSize,
                    isSelected = index == selectedIndex,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onClick = { onSelectIndex(index) }
                )
            }
        }
    }
}

@Composable
private fun SupportTierCard(
    price: String,
    label: String,
    titleFontSize: TextUnit,
    priceFontSize: TextUnit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    }

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(Radius.Lg),
        color = containerColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.Lg, horizontal = Spacing.Xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = price,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = priceFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Spacing.Xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontSize = titleFontSize,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun calculateFittingFontSize(
    texts: List<String>,
    style: TextStyle,
    maxSize: TextUnit,
    minSize: TextUnit,
    maxWidthPx: Int,
    textMeasurer: TextMeasurer
): TextUnit {
    if (maxWidthPx <= 1) return maxSize
    var size = maxSize.value
    val min = minSize.value
    val constraints = Constraints(maxWidth = maxWidthPx)
    while (size > min) {
        val allFit = texts.all { text ->
            val result = textMeasurer.measure(
                text = text,
                style = style.copy(fontSize = size.sp),
                constraints = constraints,
                maxLines = 1
            )
            !result.hasVisualOverflow && result.lineCount <= 1
        }
        if (allFit) break
        size -= 0.5f
    }
    return size.coerceAtLeast(min).sp
}

internal fun formatSupportPrice(rawPrice: String): String {
    return rawPrice.replace(Regex("[,.]00(?=\\D*$)"), "")
}
