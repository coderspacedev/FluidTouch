package com.fluidsimulation.ui.fragments

import android.annotation.*
import android.graphics.drawable.*
import android.os.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.*
import com.fluidsimulation.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ui.activity.*

class FragmentAnimation : BaseFragment<FragmentAnimationBinding>(FragmentAnimationBinding::inflate) {

    private var fluidSelected: Int = 0

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
                editPaintQuality.apply {
                    setDropDownBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorLightCard)))
                    val items = listOf("Lowest", "Low", "Medium", "High", "Very High", "Best")
                    val paintQualityAdapter = ArrayAdapter(this@context, R.layout.layout_row_item_option, items)
                    threshold = 0
                    setAdapter(paintQualityAdapter)
                    val settings = (this@context as SettingsActivity).settings
                    val gpuQuality = settings?.GPUQuality ?: 0
                    if (gpuQuality >= 0 && gpuQuality < adapter.count) {
                        setText(items[gpuQuality], false)
                    }
                    onItemClickListener = OnItemClickListener { adapterView, view, position, id ->
                        this@context.settings?.GPUQuality = position
                    }
                    setOnTouchListener { v, event ->
                        showDropDown()
                        return@setOnTouchListener true
                    }
                }
                editSimulationResolution.apply {
                    setDropDownBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorLightCard)))
                    val items = listOf("Low", "Medium (Recommended)", "High")
                    val simulationResolutionAdapter = ArrayAdapter(this@context, R.layout.layout_row_item_option, items)
                    threshold = 0
                    setAdapter(simulationResolutionAdapter)

                    val settings = (this@context as SettingsActivity).settings
                    val qualityBaseValue = settings?.QualityBaseValue ?: 0
                    if (qualityBaseValue >= 0 && qualityBaseValue < adapter.count) {
                        setText(items[qualityBaseValue], false)
                    }
                    onItemClickListener = OnItemClickListener { adapterView, view, position, id ->
                        this@context.settings?.QualityBaseValue = position
                    }
                    setOnTouchListener { v, event ->
                        showDropDown()
                        return@setOnTouchListener true
                    }
                }
                editGlowLightShadowQuality.apply {
                    setDropDownBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorLightCard)))
                    val items = listOf("Low", "Medium", "High", "Best")
                    val glowLightShadowQualityAdapter = ArrayAdapter(this@context, R.layout.layout_row_item_option, items)
                    threshold = 0
                    setAdapter(glowLightShadowQualityAdapter)
                    val settings = (this@context as SettingsActivity).settings
                    val effectsQuality = settings?.EffectsQuality ?: 0
                    if (effectsQuality >= 0 && effectsQuality < adapter.count) {
                        setText(items[effectsQuality], false)
                    }
                    onItemClickListener = OnItemClickListener { adapterView, view, position, id ->
                        this@context.settings?.EffectsQuality = position
                    }
                    setOnTouchListener { v, event ->
                        showDropDown()
                        return@setOnTouchListener true
                    }
                }
                fluidType.setOnCheckedChangeListener { group, checkedId ->
                    fluidSelected = when (checkedId) {
                        R.id.fluid_type_smoke -> 0
                        R.id.fluid_type_water -> 1
                        R.id.fluid_type_jello -> 2
                        else -> 0
                    }
                    (this@context as SettingsActivity).settings?.FluidType = fluidSelected
                }
                sliderLifeline.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.velLifetimeInt = value.toInt()
                }
                sliderSwirliness.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.swirlinessInt = value.toInt()
                }

                editSources.apply {
                    setDropDownBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorLightCard)))
                    val items = listOf("No Finger", "1 Finger", "2 Finger", "3 Finger", "4 Finger", "5 Finger")
                    val sourceFingerAdapter = ArrayAdapter(this@context, R.layout.layout_row_item_option, items)
                    threshold = 0
                    setAdapter(sourceFingerAdapter)
                    val settings = (this@context as SettingsActivity).settings
                    val numSources = settings?.NumSources ?: 0
                    if (numSources >= 0 && numSources < adapter.count) {
                        setText(items[numSources], false)
                    }
                    onItemClickListener = OnItemClickListener { adapterView, view, position, id ->
                        this@context.settings?.NumSources = position
                    }
                    setOnTouchListener { v, event ->
                        showDropDown()
                        return@setOnTouchListener true
                    }
                }
                sliderSourceSpeed.addOnChangeListener { slider, value, fromUser ->
                    (this@context as SettingsActivity).settings?.sourceSpeedInt = value.toInt()
                }
                isSourceEnableOnResume.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        (this@context as SettingsActivity).settings?.AutoOnResume = isChecked
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
                fluidSelected = settings?.FluidType ?: 0
                fluidTypeSmoke.isChecked = fluidSelected == 0
                fluidTypeWater.isChecked = fluidSelected == 1
                fluidTypeJello.isChecked = fluidSelected == 2
                sliderLifeline.value = (settings?.velLifetimeInt ?: 0).toFloat()
                sliderSwirliness.value = (settings?.swirlinessInt ?: 0).toFloat()
                sliderSourceSpeed.value = (settings?.sourceSpeedInt ?: 0).toFloat()
                isSourceEnableOnResume.isChecked = settings?.AutoOnResume ?: false
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
                FragmentAnimation().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}