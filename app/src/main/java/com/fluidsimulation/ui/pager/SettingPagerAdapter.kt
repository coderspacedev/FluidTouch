package com.fluidsimulation.ui.pager

import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.viewpager2.adapter.*
import com.fluidsimulation.ui.fragments.*

private val NUM_TABS = 6

class SettingPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FragmentPresets.newInstance()
            1 -> return FragmentAnimation.newInstance()
            2 -> return FragmentInput.newInstance()
            3 -> return FragmentPaint.newInstance()
            4 -> return FragmentParticles.newInstance()
            5 -> return FragmentEffects.newInstance()
        }
        return FragmentPresets()
    }
}