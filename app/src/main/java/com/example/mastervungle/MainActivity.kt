package com.example.mastervungle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mastervungle.databinding.ActivityMainBinding
import com.vungle.warren.*
import com.vungle.warren.error.VungleException
import com.vungle.warren.InitCallback as InitCallback1


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVungleAds()

        binding.btnBanner.setOnClickListener {
            initVungleBanner()
        }

        binding.btnNative.setOnClickListener {
            initVungleNative()
        }

        binding.btnInterstitial.setOnClickListener {
            showInterstitialVungle()
        }

    }

    fun initVungleAds(){
        binding.txtLog.append("\n Init Vungle Ads")
        Vungle.init(
                "5f9422e403dda39c97b8f93a",
                applicationContext,
                object : InitCallback1 {
                    override fun onSuccess() {
                        binding.txtLog.append("\n Vungle Initialized")
                        initVungleInterstitial()
                    }

                    override fun onError(exception: VungleException?) {
                        binding.txtLog.append("\n Vungle Initialized onError " + exception?.message)
                    }

                    override fun onAutoCacheAdAvailable(placementId: String?) {
                        binding.txtLog.append("\n Vungle onAutoCacheAdAvailable")
                    }
                })
    }

    fun initVungleBanner(){
        binding.txtLog.append("\n Vungle Banner Init")
        binding.lyBannerAds.removeAllViews()
        val adConfig = AdConfig()
        adConfig.adSize = AdConfig.AdSize.BANNER
        Banners.loadBanner("BANNER-6693863", adConfig.adSize, object :
                LoadAdCallback {
            override fun onAdLoad(id: String) {
                binding.txtLog.append("\n Vungle banner onAdLoad " + id)
                val vungleBanner = Banners.getBanner(
                        id, AdConfig.AdSize.BANNER, vunglePlayAdCallback(
                        "Banner"
                )
                )
                binding.lyBannerAds.addView(vungleBanner)
            }

            override fun onError(id: String, e: VungleException) {
                binding.txtLog.append("\n Vungle banner error : " + e.localizedMessage)
            }
        })
    }

    fun initVungleNative(){
        binding.txtLog.append("\n Vungle Native Init")
        binding.lyBannerAds.removeAllViews()

        Vungle.loadAd("NATIVE-2127402", object : LoadAdCallback {
            override fun onAdLoad(placementReferenceId: String) {
                val adConfig = AdConfig()
                adConfig.adSize = AdConfig.AdSize.VUNGLE_MREC
                adConfig.setMuted(true)
                val vungleNativeAd = Vungle.getNativeAd("NATIVE-2127402", adConfig, vunglePlayAdCallback("Native"))
                if(vungleNativeAd!=null){
                    val nativeAdView: View = vungleNativeAd.renderNativeView()
                    binding.lyBannerAds.addView(nativeAdView)
                }
            }

            override fun onError(placementReferenceId: String, exception: VungleException) {
                binding.txtLog.append("\n Unity Native onError " + exception.message)
            }
        })
    }

    fun initVungleInterstitial(){
        binding.txtLog.append("\n Vungle Interstitial Init")
        Vungle.loadAd("DEFAULT-8632945", object : LoadAdCallback {
            override fun onAdLoad(id: String) {
                binding.txtLog.append("\n Vungle interstitial onAdLoad " + id)
            }

            override fun onError(id: String, e: VungleException) {
                binding.txtLog.append("\n Vungle interstitial error : " + e.localizedMessage)
            }
        })
    }

    fun showInterstitialVungle(){
        if (Vungle.canPlayAd("DEFAULT-8632945")) {
            Vungle.playAd("DEFAULT-8632945", null, vunglePlayAdCallback("Interstitial"))
        }else{
            binding.txtLog.append("\n Interstitial Vungle not loaded")
        }
    }


    private fun vunglePlayAdCallback(identity: String) = object : PlayAdCallback {
        override fun onAdStart(id: String?) {
            binding.txtLog.append("\n Vungle " + identity + " onAdStart ")
        }

        override fun onAdEnd(id: String?, completed: Boolean, isCTAClicked: Boolean) {
            binding.txtLog.append("\n Vungle " + identity + " onAdEnd ")
        }

        override fun onAdEnd(id: String?) {
            binding.txtLog.append("\n Vungle " + identity + " onAdEnd ")
        }

        override fun onAdClick(id: String?) {
            binding.txtLog.append("\n Vungle " + identity + " onAdClick ")
        }

        override fun onAdRewarded(id: String?) {
            binding.txtLog.append("\n Vungle " + identity + " onAdRewarded ")
        }

        override fun onAdLeftApplication(id: String?) {
            binding.txtLog.append("\n Vungle " + identity + " onAdLeftApplication ")
        }

        override fun onError(id: String?, exception: VungleException?) {
            binding.txtLog.append("\n Vungle " + identity + " onError " + exception?.message)
        }

        override fun onAdViewed(id: String?) {
            binding.txtLog.append("\n Vungle " + identity + " onAdViewed ")
        }

    }

}