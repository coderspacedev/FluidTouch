package com.fluidsimulation.base

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.fluidsimulation.ext.*

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B,
        val isFullScreen: Boolean = false,
        val isFullScreenIncludeNav: Boolean = false,
        private val isLight: Boolean = false,
        private val isPadding: Boolean = false,
        private val isNavPadding: Boolean = false,
        private val isStatusBarTransparent: Boolean = false) :
        AppCompatActivity() {

    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        if (isFullScreen) window?.applyFullScreen()
        initWindows()
        binding = bindingFactory(layoutInflater)
        setContentView(binding?.root)
        initPadding()
        initView()
        initExtra()
        initListeners()
        initSize()
    }

    private fun initSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }

    abstract fun initExtra()
    abstract fun initListeners()
    abstract fun initView()

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            initWindows()
        }
    }

    private fun initWindows() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isLight
            isAppearanceLightNavigationBars = isLight
        }
    }

    private fun initPadding() {
        binding?.apply {
            root.setOnApplyWindowInsetsListener { v: View, insets: WindowInsets ->
                v.setPadding(0, isPadding.takeIf { it }?.let { getStatusBarHeight() } ?: 0, 0, isNavPadding.takeIf { it }?.let { getNavigationBarHeight() } ?: 0)
                insets
            }
        }
    }

    open fun updateStatusBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@BaseActivity, color)
    }

    open fun updateNavigationBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@BaseActivity, color)
    }

    private fun Window.applyFullScreen() {
        if (isFullScreenIncludeNav) {
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        }
        removeControlBar()
    }

    fun Window.showControlBar() {
        val windowInsetsController = WindowCompat.getInsetsController(this, decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    fun Window.removeControlBar() {
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}