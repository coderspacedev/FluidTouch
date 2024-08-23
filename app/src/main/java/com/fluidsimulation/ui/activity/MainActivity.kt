package com.fluidsimulation.ui.activity

import android.os.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*

class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate,
    isFullScreen = true,
    isStatusBarTransparent = true
) {

    override fun ActivityMainBinding.initExtra() {
        Handler(mainLooper).postDelayed({
            go(FluidActivity::class.java, finish = true)
        }, 10)
    }

    override fun ActivityMainBinding.initListeners() {}

    override fun ActivityMainBinding.initView() {}
}