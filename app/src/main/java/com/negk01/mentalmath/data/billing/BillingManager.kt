package com.negk01.mentalmath.data.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "BillingManager"
private const val RECONNECT_DELAY_MS = 2000L
private const val MAX_RECONNECT_ATTEMPTS = 3

class BillingManager(
    context: Context,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) : PurchasesUpdatedListener {
    private val _localizedPrices = MutableStateFlow<Map<String, String>>(emptyMap())
    val localizedPrices = _localizedPrices.asStateFlow()

    private val _billingEvents = MutableSharedFlow<BillingEvent>(
        extraBufferCapacity = 4,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val billingEvents = _billingEvents.asSharedFlow()

    private val productDetails = ConcurrentHashMap<String, ProductDetails>()
    private val processingTokens = ConcurrentHashMap.newKeySet<String>()

    private val billingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    private var isConnecting = false
    private var reconnectAttempts = 0

    fun startConnection() {
        if (billingClient.isReady || isConnecting) return
        isConnecting = true

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                isConnecting = false
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    reconnectAttempts = 0
                    loadProducts()
                    consumePendingPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnecting = false
                if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
                    reconnectAttempts++
                    scope.launch {
                        delay(RECONNECT_DELAY_MS)
                        startConnection()
                    }
                }
            }
        })
    }

    private fun loadProducts() {
        scope.launch(Dispatchers.IO) {
            try {
                val products = BillingConstants.ALL_TIPS.map { id ->
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(id)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                }

                val params = QueryProductDetailsParams.newBuilder()
                    .setProductList(products)
                    .build()

                val result = billingClient.queryProductDetails(params)
                if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val details = result.productDetailsList.orEmpty()
                    productDetails.putAll(details.associateBy { it.productId })
                    _localizedPrices.value = details.mapNotNull { item ->
                        item.oneTimePurchaseOfferDetails?.formattedPrice?.let { item.productId to it }
                    }.toMap()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load product details", e)
            }
        }
    }

    private fun consumePendingPurchases() {
        scope.launch(Dispatchers.IO) {
            try {
                val params = QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()

                val result = billingClient.queryPurchasesAsync(params)
                if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    result.purchasesList
                        .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                        .forEach { purchase ->
                            processPurchase(purchase)
                        }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to consume pending purchases", e)
            }
        }
    }

    fun launchPurchase(activity: Activity, productId: String) {
        if (!billingClient.isReady) {
            startConnection()
            _billingEvents.tryEmit(BillingEvent.Unavailable)
            return
        }

        val details = productDetails[productId] ?: run {
            _billingEvents.tryEmit(BillingEvent.Unavailable)
            return
        }

        val productParams = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        )

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productParams)
            .build()

        val result = billingClient.launchBillingFlow(activity, flowParams)
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> Unit
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> _billingEvents.tryEmit(BillingEvent.Unavailable)
            BillingClient.BillingResponseCode.USER_CANCELED -> _billingEvents.tryEmit(BillingEvent.Canceled)
            else -> _billingEvents.tryEmit(BillingEvent.Error(result.debugMessage))
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: List<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    when (purchase.purchaseState) {
                        Purchase.PurchaseState.PURCHASED -> processPurchase(purchase)
                        Purchase.PurchaseState.PENDING -> _billingEvents.tryEmit(BillingEvent.Pending)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _billingEvents.tryEmit(BillingEvent.Canceled)
            }
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                _billingEvents.tryEmit(BillingEvent.Unavailable)
            }
            else -> {
                _billingEvents.tryEmit(BillingEvent.Error(result.debugMessage))
            }
        }
    }

    private fun processPurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return
        val token = purchase.purchaseToken
        if (!processingTokens.add(token)) return

        scope.launch(Dispatchers.IO) {
            try {
                withContext(NonCancellable) {
                    val result = consume(token)
                    val debugMessage = result.debugMessage
                    when (result.responseCode) {
                        BillingClient.BillingResponseCode.OK -> {
                            val productId = purchase.products.firstOrNull().orEmpty()
                            _billingEvents.tryEmit(BillingEvent.Success(productId, purchase.quantity))
                        }
                        BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                            _billingEvents.tryEmit(BillingEvent.Unavailable)
                        }
                        else -> {
                            _billingEvents.tryEmit(BillingEvent.Error(debugMessage))
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to process purchase: ${e.message}")
                _billingEvents.tryEmit(BillingEvent.Error(e.message))
            } finally {
                processingTokens.remove(token)
            }
        }
    }

    private suspend fun consume(token: String): BillingResult {
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(token)
            .build()
        return billingClient.consumePurchase(params).billingResult
    }

    fun destroy() {
        isConnecting = false
        reconnectAttempts = 0
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
        scope.cancel()
    }
}
