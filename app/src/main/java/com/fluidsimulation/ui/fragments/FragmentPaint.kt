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

class FragmentPaint : BaseFragment<FragmentPaintBinding>(FragmentPaintBinding::inflate) {

    private var colorOptionSelected: Int = 0
    private var colorChangeSelected: Int = 1

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

                actionColors.setOnCheckedChangeListener { group, checkedId ->
                    colorOptionSelected = when (checkedId) {
                        R.id.action_random_colors -> 0
                        R.id.action_trippy_color -> 3
                        else -> 0
                    }
                    (this@context as SettingsActivity).settings?.ColorOption = colorOptionSelected
                }
                actionColorChange.setOnCheckedChangeListener { group, checkedId ->
                    colorChangeSelected = when (checkedId) {
                        R.id.action_with_new_touch -> 0
                        R.id.action_every_moment -> 1
                        else -> 0
                    }
                    (this@context as SettingsActivity).settings?.ColorChange = colorChangeSelected
                }
                sliderAmount.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.forceInt = value.toInt()
                }
                sliderLifetime.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.inputSizeInt = value.toInt()
                }

                isColorsSaturateWhite.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        (this@context as SettingsActivity).settings?.OverbrightColors = isChecked
                    }
                }
                isInvertColors.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        (this@context as SettingsActivity).settings?.InvertColors = isChecked
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
                colorOptionSelected = settings?.ColorOption ?: 0
                colorChangeSelected = settings?.ColorChange ?: 0
                actionRandomColors.isChecked = colorOptionSelected == 0
                actionTrippyColor.isChecked = colorOptionSelected == 3
                actionWithNewTouch.isChecked = colorChangeSelected == 0
                actionEveryMoment.isChecked = colorChangeSelected == 1
                sliderAmount.value = (settings?.fluidAmountInt ?: 0).toFloat()
                sliderLifetime.value = (settings?.fluidLifeTimeInt ?: 0).toFloat()
                isColorsSaturateWhite.isChecked = settings?.OverbrightColors ?: false
                isInvertColors.isChecked = settings?.InvertColors ?: false
            }
        }
    }

    override fun initListeners() {

    }

    override fun initView() {

    }

    companion object {

        private const val TAG = "FragmentPaint"
        fun newInstance() =
                FragmentPaint().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}