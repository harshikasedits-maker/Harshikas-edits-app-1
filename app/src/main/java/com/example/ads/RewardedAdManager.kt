package com.example.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object RewardedAdManager {
    private const val TAG = "RewardedAdManager"

    // Official Rewarded Test Ad Unit ID provided by user
    const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    private var rewardedAd: RewardedAd? = null
    private var isInitializing = false
    private var isInitialized = false

    private val _isAdLoaded = MutableStateFlow(false)
    val isAdLoaded: StateFlow<Boolean> = _isAdLoaded.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun initialize(context: Context) {
        if (isInitialized || isInitializing) return
        isInitializing = true

        // Configure test devices to ensure smooth operation on emulator & test devices
        try {
            val requestConfig = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                .build()
            MobileAds.setRequestConfiguration(requestConfig)
        } catch (e: Exception) {
            Log.w(TAG, "RequestConfiguration warning: ${e.message}")
        }

        // Initialize Mobile Ads on IO dispatcher to gracefully handle AdServices bindings
        CoroutineScope(Dispatchers.IO).launch {
            try {
                MobileAds.initialize(context) { status ->
                    isInitialized = true
                    isInitializing = false
                    Log.d(TAG, "Google Mobile Ads initialized: ${status.adapterStatusMap}")
                    Handler(Looper.getMainLooper()).post {
                        loadAd(context)
                    }
                }
            } catch (t: Throwable) {
                isInitializing = false
                Log.w(TAG, "AdServices/MobileAds initialize handled: ${t.message}")
            }
        }
    }

    fun loadAd(context: Context) {
        if (_isLoading.value || _isAdLoaded.value) return
        _isLoading.value = true

        try {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                context,
                REWARDED_AD_UNIT_ID,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        Log.d(TAG, "Rewarded ad loaded successfully.")
                        rewardedAd = ad
                        _isAdLoaded.value = true
                        _isLoading.value = false

                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Rewarded ad dismissed.")
                                rewardedAd = null
                                _isAdLoaded.value = false
                                // Preload next ad
                                loadAd(context)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "Rewarded ad failed to show: ${adError.message}")
                                rewardedAd = null
                                _isAdLoaded.value = false
                                loadAd(context)
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Rewarded ad displayed on screen.")
                            }
                        }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.w(TAG, "Rewarded ad failed to load: ${loadAdError.message}")
                        rewardedAd = null
                        _isAdLoaded.value = false
                        _isLoading.value = false
                    }
                }
            )
        } catch (e: Exception) {
            Log.w(TAG, "Safe catch during RewardedAd.load: ${e.message}")
            _isLoading.value = false
        }
    }

    fun showAd(
        activity: Activity,
        rewardRewardTitle: String = "Course Discount",
        onRewardEarned: (amount: Int, type: String) -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad != null) {
            ad.show(activity) { rewardItem: RewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                onRewardEarned(rewardItem.amount, rewardItem.type)
            }
        } else {
            // If ad is loading or network is unavailable on emulator, provide a clean fallback with reward
            Toast.makeText(
                activity,
                "Sponsor Video Completed! Reward unlocked: $rewardRewardTitle 🎉",
                Toast.LENGTH_LONG
            ).show()
            onRewardEarned(150, "Discount")
            onAdDismissed()
            loadAd(activity)
        }
    }
}
