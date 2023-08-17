package com.fluidsimulation.ui.fragments

import android.annotation.*
import android.graphics.drawable.*
import android.os.*
import android.widget.*
import androidx.core.content.*
import com.fluidsimulation.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ui.activity.*

class FragmentEffects : BaseFragment<FragmentEffectsBinding>(FragmentEffectsBinding::inflate) {

    private var borderModeSelected: Int = 0
    private var qualitySelected: Int = 0

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
                viewSelectedColor.setOnClickListener { }
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
                            (this@context as SettingsActivity).settings?.UseDetailTexture = true
                        }

                        else -> {
                            (this@context as SettingsActivity).settings?.UseDetailTexture = false
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
    }

    private fun initDefault() {
        activity?.apply context@{
            binding?.apply {
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
    }

    override fun initListeners() {

    }

    override fun initView() {

    }

    companion object {

        private const val TAG = "FragmentAnimation"
        fun newInstance() =
                FragmentEffects().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}