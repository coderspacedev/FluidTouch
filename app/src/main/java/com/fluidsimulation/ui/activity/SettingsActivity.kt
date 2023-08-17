package com.fluidsimulation.ui.activity

import androidx.activity.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.model.*
import com.fluidsimulation.ui.pager.*
import com.google.android.material.tabs.*
import com.magicfluids.*

class SettingsActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    private val tabTitles get() = mutableListOf("PRESETS", "ANIMATION", "INPUT", "PAINT", "PARTICLES", "EFFECT")
    var settings: Settings? = null
    override fun initExtra() {
        settings = Settings.Current
        initPager()
    }

    private fun initPager() {
        binding?.apply {
            val pagerAdapter = SettingPagerAdapter(supportFragmentManager, lifecycle)
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = 6
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

    override fun initListeners() {

    }

    override fun initView() {
        binding?.apply {
            toolbar.title = "Settings"
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        onBackPressedDispatcher.addCallback(this@SettingsActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                settings?.let {
                    Settings.LWPCurrent?.setEverythingFrom(it)
                    Settings.Current?.setEverythingFrom(it)
                }

                settings?.let { saveSessionSettings(it, SETTINGS_NAME) }
                Settings.Current?.ReloadRequired = true
                Settings.Current?.ReloadRequiredPreview = true
                Settings.LWPCurrent?.ReloadRequired = true
                Settings.LWPCurrent?.ReloadRequiredPreview = true
                finish()
            }
        })
    }
}