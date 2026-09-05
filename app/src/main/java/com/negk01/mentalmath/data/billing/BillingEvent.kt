package com.negk01.mentalmath.data.billing

sealed interface BillingEvent {
    data class Success(val formattedPrice: String) : BillingEvent
    data object Canceled : BillingEvent
    data class Error(val message: String? = null) : BillingEvent
    data object Unavailable : BillingEvent
}
