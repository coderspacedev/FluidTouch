package com.fluidsimulation.ui.fragments

import android.annotation.*
import com.fluidsimulation.*
import com.fluidsimulation.base.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.ui.activity.*

class FragmentInput : BaseFragment<FragmentInputBinding>(FragmentInputBinding::inflate) {

    private var swipeActionSelected: Int = 0
    private var tapActionSelected: Int = 0

    override fun create() {}

    override fun FragmentInputBinding.viewCreated() {}

    private fun FragmentInputBinding.initDefault() {
        activity?.apply context@{
            tabSwipe.isSelected = true
            tabTap.isSelected = false
            layoutTouch.beGone()
            layoutSwipe.beVisible()
            val settings = (this@context as SettingsActivity).settings
            swipeActionSelected = settings?.InputSwipeMode ?: 0
            actionStream.isChecked = swipeActionSelected == 0
            actionInverseStream.isChecked = swipeActionSelected == 1
            sliderSwipeForce.value = (settings?.forceInt ?: 0).toFloat()
            sliderSwipeSize.value = (settings?.inputSizeInt ?: 0).toFloat()
            isConstantForce.isChecked = settings?.InputSwipeConstant ?: false

            tapActionSelected = settings?.InputTouchMode ?: 0
            actionStreamSource.isChecked = tapActionSelected == 0
            actionVortex.isChecked = tapActionSelected == 1
            action2WayBlower.isChecked = tapActionSelected == 2
            actionRotatingBlower.isChecked = tapActionSelected == 4
            actionSource.isChecked = tapActionSelected == 5
            actionSink.isChecked = tapActionSelected == 6
            actionSourceSink.isChecked = tapActionSelected == 7
            actionNone.isChecked = tapActionSelected == 8
            sliderTapForce.value = (settings?.touchInputForceInt ?: 0).toFloat()
            sliderTapSize.value = (settings?.touchInputSizeInt ?: 0).toFloat()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun FragmentInputBinding.initListeners() {
        activity?.apply context@{
            tabSwipe.setOnClickListener {
                tabSwipe.isSelected = true
                tabTap.isSelected = false
                layoutTouch.beGone()
                layoutSwipe.beVisible()
            }
            tabTap.setOnClickListener {
                tabSwipe.isSelected = false
                tabTap.isSelected = true
                layoutSwipe.beGone()
                layoutTouch.beVisible()
            }
            actionSwipe.setOnCheckedChangeListener { group, checkedId ->
                swipeActionSelected = when (checkedId) {
                    R.id.action_inverse_stream -> 1
                    else -> 0
                }
                (this@context as SettingsActivity).settings?.InputSwipeMode = swipeActionSelected
            }
            sliderSwipeForce.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.forceInt = value.toInt()
            }
            sliderSwipeSize.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.inputSizeInt = value.toInt()
            }
            actionTap.setOnCheckedChangeListener { group, checkedId ->
                tapActionSelected = when (checkedId) {
                    R.id.action_stream_source -> 0
                    R.id.action_vortex -> 1
                    R.id.action_2_way_blower -> 2
                    R.id.action_rotating_blower -> 4
                    R.id.action_source -> 5
                    R.id.action_sink -> 6
                    R.id.action_source_sink -> 7
                    R.id.action_none -> 8
                    else -> 0
                }
                (this@context as SettingsActivity).settings?.InputTouchMode = tapActionSelected
            }
            sliderTapForce.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.touchInputForceInt = value.toInt()
            }
            sliderTapSize.addOnChangeListener { slider, value, fromUser ->
                (this@context as SettingsActivity).settings?.touchInputSizeInt = value.toInt()
            }
            isConstantForce.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    (this@context as SettingsActivity).settings?.InputSwipeConstant = isChecked
                }
            }
            initDefault()
        }
    }

    override fun FragmentInputBinding.initView() {}

    companion object {

        private const val TAG = "FragmentInput"
        fun newInstance() = FragmentInput()
    }
}