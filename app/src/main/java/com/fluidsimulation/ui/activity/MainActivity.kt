package com.fluidsimulation.ui.activity

import android.os.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate, isFullScreen = true, isStatusBarTransparent = true) {

    override fun initExtra() {
        Handler(mainLooper).postDelayed({
            go(FluidActivity::class.java, finish = true)
        }, 1000)
    }

    override fun initListeners() {

    }

    override fun initView() {

    }
}