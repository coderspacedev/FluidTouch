package com.fluidsimulation.ui.fragments

import android.annotation.*
import android.content.res.*
import android.graphics.*
import android.graphics.drawable.*
import android.os.*
import android.text.*
import android.view.*
import android.widget.*
import androidx.core.content.*
import androidx.core.graphics.*
import androidx.core.widget.*
import com.fluidsimulation.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.listeners.*
import com.fluidsimulation.ui.activity.*
import com.google.android.material.bottomsheet.*

class FragmentParticles : BaseFragment<FragmentParticlesBinding>(FragmentParticlesBinding::inflate) {

    private var shapeSelected: Int = 0
    private var modeSelected: Int = 0
    private var colorSelected: Int = 0

    override fun create() {
        arguments?.let {

        }
    }

    override fun viewCreated() {
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

    var changeHexTextByUser: Boolean = true

    private fun chooseColor(color: Int, listener: ColorPickListener?) {
        var textColor: Int = color
        activity?.apply {
            val dialog = BottomSheetDialog(
                    this, R.style.Theme_WallpaperGallery_BottomSheetDialogTheme
            )
            val bind: LayoutDialogColorPickerBinding = LayoutDialogColorPickerBinding.inflate(layoutInflater)
            bind.apply {
                dialog.setContentView(root)
                changeHexTextByUser = false
                editHexCode.setText("%08X".format(textColor))
                colorPreview.setCardBackgroundColor(textColor)
                changeHexTextByUser = true
                svView.onColorChanged = {
                    textColor = it
                    changeHexTextByUser = false
                    editHexCode.setText("%08X".format(textColor))
                    changeHexTextByUser = true
                    seekAlpha.setMaxColor(textColor)
                    colorPreview.setCardBackgroundColor(textColor)
                }
                hueView.onHueChanged = {
                    textColor = hsvToColor(it, svView.saturation, svView.value)
                    svView.setHue(it)
                    seekAlpha.setMaxColor(textColor)
                    changeHexTextByUser = false
                    editHexCode.setText("%08X".format(textColor))
                    changeHexTextByUser = true

                    colorPreview.setCardBackgroundColor(textColor)
                }
                seekAlpha.setValue(textColor.alpha)
                seekAlpha.onValueChanged = { value, fromUser ->
                    if (fromUser) {
                        seekAlpha.setValue(value)
                        textColor = textColor.setAlpha(value)
                        changeHexTextByUser = false
                        editHexCode.setText("%08X".format(textColor))
                        changeHexTextByUser = true

                        colorPreview.setCardBackgroundColor(textColor)
                    }
                }

                editHexCode.filters = arrayOf(HexadecimalFilter(), InputFilter.LengthFilter(8))
                editHexCode.doOnTextChanged { text, start, before, count ->
                    if (!changeHexTextByUser) {
                        return@doOnTextChanged
                    }

                    try {
                        textColor = Color.parseColor("#$text")
                        seekAlpha.setValue(textColor.alpha)
                        seekAlpha.setMaxColor(textColor)
                        colorPreview.setCardBackgroundColor(textColor)
                    } catch (e: IllegalArgumentException) {

                    }
                }
                buttonTextColorUpdate.setOnClickListener {
                    listener?.color(textColor)
                    dialog.dismiss()
                }
                buttonTextColorCancel.setOnClickListener {
                    dialog.dismiss()
                }

                (root.parent as View).backgroundTintMode = PorterDuff.Mode.CLEAR
                (root.parent as View).backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                (root.parent as View).setBackgroundColor(Color.TRANSPARENT)
            }

            val params = dialog.window?.attributes
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT
            params?.gravity = Gravity.CENTER
            dialog.window?.attributes = params
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.setDimAmount(.34f)
            dialog.window?.navigationBarColor = ContextCompat.getColor(this, R.color.colorBlack)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (!isFinishing || !isDestroyed) {
                dialog.show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListeners() {
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
                buttonChooseBackground.setOnClickListener {
                    chooseColor((this@context as SettingsActivity).settings?.ParticlesColor ?: Color.WHITE, object : ColorPickListener {
                        override fun color(color: Int) {
                            super.color(color)
                            this@context.settings?.ParticlesColor = color
                            viewSelectedColor.setCardBackgroundColor(color)
                        }
                    })
                }
                initDefault()
            }
        }
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