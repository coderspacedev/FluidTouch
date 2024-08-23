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

    override fun ActivitySettingsBinding.initExtra() {
        settings = Settings.Current
    }

    override fun onResume() {
        super.onResume()
        binding?.initPager()
    }

    private fun ActivitySettingsBinding.initPager() {
        val pagerAdapter = SettingPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun ActivitySettingsBinding.initListeners() {}

    override fun ActivitySettingsBinding.initView() {
        toolbar.title = "Settings"
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this@SettingsActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                settings?.let {
                    Settings.Current?.setEverythingFrom(it)
                }

                settings?.let { saveSessionSettings(it, DEFAULT_SETTING) }
                Settings.Current?.ReloadRequired = true
                Settings.Current?.ReloadRequiredPreview = true
                finish()
            }
        })
    }
}