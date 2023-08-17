package com.magicfluids

object QualitySetting {

    var EffectsSettings: ArrayList<String>? = null
    var PaintResSettings: ArrayList<String>? = null
    var SimResSettings: ArrayList<String>? = null
    fun init() {
        if (SimResSettings == null) {
            SimResSettings = ArrayList()
            SimResSettings?.add("Low")
            SimResSettings?.add("Medium (recommended)")
            SimResSettings?.add("High")
            PaintResSettings = ArrayList()
            PaintResSettings?.add("Lowest")
            PaintResSettings?.add("Low")
            PaintResSettings?.add("Medium")
            PaintResSettings?.add("High")
            PaintResSettings?.add("Very high")
            PaintResSettings?.add("Best")
            EffectsSettings = ArrayList()
            EffectsSettings?.add("Low")
            EffectsSettings?.add("Medium")
            EffectsSettings?.add("High")
            EffectsSettings?.add("Best")
        }
    }

    fun setQualitySettingsToDefault(settings: Settings) {
        settings.QualityBaseValue = 1
        settings.GPUQuality = 2
        settings.EffectsQuality = 1
    }

    private val numberOfCores: Int
        private get() = Runtime.getRuntime().availableProcessors()

    fun setQualitySettingsFromPerf(settings: Settings, nativeInterface: NativeInterface?) {
        settings.QualityBaseValue = 1
        nativeInterface?.perfHeuristic()
        val numberOfCores = numberOfCores
        if (numberOfCores <= 2) {
            settings.GPUQuality = 2
            settings.EffectsQuality = 1
            settings.QualityBaseValue = 0
        } else if (numberOfCores <= 4) {
            settings.GPUQuality = 3
            settings.EffectsQuality = 1
        } else {
            settings.GPUQuality = 3
            settings.EffectsQuality = 2
        }
    }
}