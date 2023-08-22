package com.fluidsimulation.ui.activity

import android.webkit.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*

class FluidBackgroundActivity : BaseActivity<ActivityFluidBackgroundBinding>(ActivityFluidBackgroundBinding::inflate) {

    override fun initExtra() {
        initWebView()
    }

    private fun initWebView() {
        binding?.apply {
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webView.loadUrl("file:///android_asset/web/index.html")
            webView.webViewClient = WebViewClient()
        }
    }

    override fun initListeners() {

    }

    override fun initView() {

    }
}