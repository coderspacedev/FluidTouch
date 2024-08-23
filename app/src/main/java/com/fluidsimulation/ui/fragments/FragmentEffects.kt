package com.fluidsimulation.ui.fragments

import android.annotation.*
import android.content.res.*
import android.graphics.*
import android.graphics.drawable.*
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

class FragmentEffects : BaseFragment<FragmentEffectsBinding>(FragmentEffectsBinding::inflate) {

    private var borderModeSelected: Int = 0

    override fun create() {}

    override fun FragmentEffectsBinding.viewCreated() {}

    private fun FragmentEffectsBinding.initDefault() {
        activity?.apply context@{
            val settings = (this@context as SettingsActivity).settings

            borderModeSelected = settings?.BorderMode ?: 0
            actionWall.isChecked = borderModeSelected == 0
            actionWrap.isChecked = borderModeSelected == 1
            actionWrapMirror.isChecked = borderModeSelected == 2

            sliderGravity.value = (settings?.gravityInt ?: 0).toFloat()

            isGlow.isChecked = settings?.Glow ?: false

            sliderNear.value = (settings?.glowLevelStrength0Int ?: 0).toFloat()
            sliderMid.value = (settings?.glowLevelStrength1Int ?: 0).toFloat()
            sliderFar.value = (settings?.glowLevelStrength2Int ?: 0).toFloat()
            sliderThreshold.value = (settings?.glowThresholdInt ?: 0).toFloat()
            sliderParticleLighting.value = (settings?.getIntFrom01Value(settings.GlowParticleIntensity) ?: 0F).toFloat()

            isLight.isChecked = settings?.LightSource ?: false
            sliderLightIntensity.value = (settings?.lightIntensityInt ?: 0).toFloat()
            sliderLightSize.value = (settings?.lightRadiusInt ?: 0).toFloat()

            isShadow.isChecked = settings?.ShadowSource ?: false
            isSelf.isChecked = settings?.ShadowSelf ?: false
            isInverse.isChecked = settings?.ShadowInverse ?: false
            sliderShadowIntensity.value = (settings?.shadowIntensityInt ?: 0).toFloat()
            sliderShadowLength.value = (settings?.shadowFalloffLengthInt ?: 0).toFloat()
            sliderShadowSpeed.value = (settings?.lightSourceSpeedInt ?: 0).toFloat()
            sliderShadowPosX.value = (settings?.lightSourcePosXInt ?: 0).toFloat()
            sliderShadowPosY.value = (settings?.lightSourcePosYInt ?: 0).toFloat()

            isFluidTexture.isChecked = settings?.UseDetailTexture ?: false
            sliderScale.value = (settings?.detailUVScaleInt ?: 0).toFloat()
            if (settings?.DetailHD == true) {
                fluidHighQuality.isChecked = true
            } else {
                fluidNormalQuality.isChecked = true
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
    override fun FragmentEffectsBinding.initListeners() {
        activity?.apply context@{
            actionEdgeMode.setOnCheckedChangeListener { group, checkedId ->
                borderModeSelected = when (checkedId) {
                    R.id.action_wall -> 0
                    R.id.action_wrap -> 1
                    R.id.action_wrap_mirror -> 2
                    else -> 0
                }
                (this@context as SettingsActivity).settings?.BorderMode = borderModeSelected
            }

            sliderGravity.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.gravityInt = value.toInt()
            }

            isGlow.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.Glow = isChecked
                }
            }

            sliderNear.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.glowLevelStrength0Int = value.toInt()
            }
            sliderMid.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.glowLevelStrength1Int = value.toInt()
            }
            sliderFar.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.glowLevelStrength2Int = value.toInt()
            }
            sliderThreshold.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.glowThresholdInt = value.toInt()
            }
            sliderParticleLighting.addOnChangeListener { slider, value, fromUser ->
                val settings = (this@context as SettingsActivity).settings
                settings?.GlowParticleIntensity = settings?.set01ValueFromInt(value) ?: 0F
            }

            isLight.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.LightSource = isChecked
                }
            }
            viewSelectedColor.setOnClickListener {
                chooseColor((this@context as SettingsActivity).settings?.LightColor ?: Color.WHITE, object : ColorPickListener {
                    override fun color(color: Int) {
                        super.color(color)
                        this@context.settings?.LightColor = color
                        viewSelectedColor.setCardBackgroundColor(color)
                    }
                })
            }
            sliderLightIntensity.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.lightIntensityInt = value.toInt()
            }
            sliderLightSize.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.lightRadiusInt = value.toInt()
            }

            isShadow.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.ShadowSource = isChecked
                }
            }
            isSelf.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.ShadowSelf = isChecked
                }
            }
            isInverse.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.ShadowInverse = isChecked
                }
            }

            sliderShadowIntensity.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.shadowIntensityInt = value.toInt()
            }
            sliderLightSize.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.shadowFalloffLengthInt = value.toInt()
            }
            sliderShadowLength.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.lightSourceSpeedInt = value.toInt()
            }
            sliderShadowPosX.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.lightSourcePosXInt = value.toInt()
            }
            sliderShadowPosY.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.lightSourcePosYInt = value.toInt()
            }

            isFluidTexture.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.UseDetailTexture = isChecked
                }
            }
            sliderScale.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.detailUVScaleInt = value.toInt()
            }
            fluidTextureQuality.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.fluid_high_quality -> {
                        (this@context as SettingsActivity).settings?.DetailHD = true
                    }

                    else -> {
                        (this@context as SettingsActivity).settings?.DetailHD = false
                    }
                }
            }
            editFluidTexture.apply {
                setDropDownBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorLightCard)))
                val items = listOf("Water", "Fire", "Ice", "Cloud", "Cloud 1", "Veins", "Something 1", "Something 2", "Something 3", "Something 4")
                val sourceFingerAdapter = ArrayAdapter(this@context, R.layout.layout_row_item_option, items)
                threshold = 0
                setAdapter(sourceFingerAdapter)
                val settings = (this@context as SettingsActivity).settings
                val numSources = settings?.DetailTexture ?: 0
                if (numSources >= 0 && numSources < adapter.count) {
                    setText(items[numSources], false)
                }
                onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                    this@context.settings?.DetailTexture = position
                }
                setOnTouchListener { v, event ->
                    showDropDown()
                    return@setOnTouchListener true
                }
            }
            initDefault()
        }
    }

    override fun FragmentEffectsBinding.initView() {}

    companion object {

        private const val TAG = "FragmentAnimation"
        fun newInstance() = FragmentEffects()
    }
}