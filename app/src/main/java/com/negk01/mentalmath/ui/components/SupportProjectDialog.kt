package com.negk01.mentalmath.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.data.billing.BillingConstants
import com.negk01.mentalmath.data.billing.BillingEvent
import com.negk01.mentalmath.data.billing.BillingManager
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.findActivity

data class SupportTier(
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

    var isSubmitting by rememberSaveable { mutableStateOf(false) }

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

    val onSelectTier: (String) -> Unit = { productId ->
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

    val tier1Price = localizedPrices[BillingConstants.TIP_SMALL] ?: stringResource(R.string.support_tier_1_price)
    val tier2Price = localizedPrices[BillingConstants.TIP_MEDIUM] ?: stringResource(R.string.support_tier_2_price)
    val tier3Price = localizedPrices[BillingConstants.TIP_LARGE] ?: stringResource(R.string.support_tier_3_price)

    val tiers = listOf(
        SupportTier(BillingConstants.TIP_SMALL, tier1Price, stringResource(R.string.support_tier_1_label)),
        SupportTier(BillingConstants.TIP_MEDIUM, tier2Price, stringResource(R.string.support_tier_2_label)),
        SupportTier(BillingConstants.TIP_LARGE, tier3Price, stringResource(R.string.support_tier_3_label))
    )
    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }

    AppDialog(
        onDismiss = onDismiss,
        dismissOnScrimTap = true,
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
                modifier = Modifier.size(24.dp)
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
            enabled = !isSubmitting,
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
fun SupportTiersGrid(
    tiers: List<SupportTier>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Sm)
    ) {
        tiers.forEachIndexed { index, tier ->
            SupportTierCard(
                price = tier.price,
                label = tier.label,
                isSelected = index == selectedIndex,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { onSelectIndex(index) }
            )
        }
    }
}

@Composable
fun SupportTierCard(
    price: String,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
    }

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(Radius.Md),
        color = containerColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.Md, horizontal = Spacing.Xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Spacing.Xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}
