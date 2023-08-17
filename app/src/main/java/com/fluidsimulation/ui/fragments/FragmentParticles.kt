package com.fluidsimulation.ui.fragments

import android.annotation.*
import android.graphics.drawable.*
import android.os.*
import android.widget.*
import androidx.core.content.*
import com.fluidsimulation.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.ui.activity.*

class FragmentParticles : BaseFragment<FragmentParticlesBinding>(FragmentParticlesBinding::inflate) {

    private var shapeSelected: Int = 0
    private var modeSelected: Int = 0
    private var colorSelected: Int = 0

    override fun create() {
        arguments?.let {

        }
    }

    override fun viewCreated() {
        initAdapter()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        activity?.apply context@{
            binding?.apply {
                actionShape.setOnCheckedChangeListener { group, checkedId ->
                    shapeSelected = when (checkedId) {
                        R.id.action_dots -> 0
                        R.id.action_lines -> 1
                        R.id.action_stars -> 2
                        else -> 0
                    }
                    (this@context as SettingsActivity).settings?.ParticlesShape = shapeSelected
                }

                actionMode.setOnCheckedChangeListener { group, checkedId ->
                    modeSelected = when (checkedId) {
                        R.id.action_add_on_touch -> 0
                        R.id.action_fill_screen -> 1
                        else -> 0
                    }
                    (this@context as SettingsActivity).settings?.ParticlesMode = modeSelected
                }

                actionColors.setOnCheckedChangeListener { group, checkedId ->
                    colorSelected = when (checkedId) {
                        R.id.action_use_paint_colors -> {
                            layoutColorPalette.beGone()
                            0
                        }
                        R.id.action_use_separate_color -> {
                            layoutColorPalette.beVisible()
                            1
                        }
                        else -> {
                            layoutColorPalette.beGone()
                            0
                        }
                    }
                    (this@context as SettingsActivity).settings?.ParticlesUsePaintColor = colorSelected == 0
                }

                sliderAmount.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.particlesLifeTimeMsInt = value.toInt()
                }
                sliderLifetime.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.particlesPerSecInt = value.toInt()
                }
                sliderSize.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.particlesSizeInt = value.toInt()
                }

                isParticles.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        (this@context as SettingsActivity).settings?.ParticlesEnabled = isChecked
                    }
                }
                initDefault()
            }
        }
    }

    private fun initDefault() {
        activity?.apply context@{
            binding?.apply {
                val settings = (this@context as SettingsActivity).settings
                isParticles.isChecked = settings?.ParticlesEnabled ?: false
                shapeSelected = settings?.ParticlesShape ?: 0
                modeSelected = settings?.ParticlesMode ?: 0
                colorSelected = if (settings?.ParticlesUsePaintColor != false) 0 else 1
                actionDots.isChecked = shapeSelected == 0
                actionLines.isChecked = shapeSelected == 1
                actionStars.isChecked = shapeSelected == 2
                actionAddOnTouch.isChecked = modeSelected == 0
                actionFillScreen.isChecked = modeSelected == 1
                actionUsePaintColors.isChecked = colorSelected == 0
                actionUseSeparateColor.isChecked = colorSelected == 1

                sliderAmount.value = (settings?.particlesLifeTimeMsInt ?: 0).toFloat()
                sliderLifetime.value = (settings?.particlesPerSecInt ?: 0).toFloat()
                sliderSize.value = (settings?.particlesSizeInt ?: 0).toFloat()

                settings?.ParticlesColor?.let { viewSelectedColor.setCardBackgroundColor(it) }
            }
        }
    }

    override fun initListeners() {

    }

    override fun initView() {

    }

    companion object {

        private const val TAG = "FragmentParticles"
        fun newInstance() =
                FragmentParticles().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}