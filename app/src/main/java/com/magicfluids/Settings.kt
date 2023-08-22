package com.magicfluids

import com.fluidsimulation.*

class Settings {

    var BORDER_WALL = 0
    var AllowMultithreading = false
    var AutoOnResume = false
    var BackgroundColor = 0
    var BorderMode = 0
    var Color0 = 0
    var Color1 = 0
    var Color2 = 0
    var Color3 = 0
    var Color4 = 0
    var Color5 = 0
    var ColorChange = 0
    var ColorOption = 0
    var DColor0 = 0
    var DColor1 = 0
    var DColor2 = 0
    var DetailHD = false
    var DetailTexture = 0
    var DetailUVScale = 0f
    var EffectsQuality = 0
    var FPSLimit = 0
    var FluidAmount = 0f
    var FluidLifeTime = 0f
    var FluidType = 0
    var Force = 0f
    var GPUQuality = 0
    var Glow = false
    var GlowLevelStrength0 = 0f
    var GlowLevelStrength1 = 0f
    var GlowLevelStrength2 = 0f
    var GlowParticleIntensity = 0f
    var GlowThreshold = 0f
    var Gravity = 0f
    var InputSize = 0f
    var InputSwipeConstant = false
    var InputSwipeMode = 0
    var InputTouchMode = 0
    var InvertColors = false
    var LightColor = 0
    var LightIntensity = 0f
    var LightRadius = 0f
    var LightSource = false
    var LightSourcePosX = 0f
    var LightSourcePosY = 0f
    var LightSourceSpeed = 0f
    var MenuButtonVisibility = 0
    var NumColors = 0
    var NumDColors = 0
    var NumSources = 0
    var OverbrightColors = false
    var ParticlesColor = 0
    var ParticlesEnabled = false
    var ParticlesLifeTimeSec = 0f
    var ParticlesMode = 0
    var ParticlesPerSec = 0f
    var ParticlesShape = 0
    var ParticlesSize = 0f
    var ParticlesUsePaintColor = false
    var QualityBaseValue = 0
    var ShadowFalloffLength = 0f
    var ShadowIntensity = 0f
    var ShadowInverse = false
    var ShadowSelf = false
    var ShadowSource = false
    var SourceSpeed = 0f
    var Swirliness = 0f
    var TouchInputForce = 0f
    var TouchInputSize = 0f
    var UseDetailTexture = false
    var VelLifetime = 0f
    var Colors = IntArray(6)
    var ColorsActive = BooleanArray(6)
    var DColors = IntArray(3)
    var DColorsActive = BooleanArray(3)
    var GPUAnimAvailable = true
    var ReloadRequired = false
    var ReloadRequiredPreview = false

    private val tmpColors = IntArray(6)

    fun getIntValue(f: Float, f2: Float, f3: Float): Int {
        return ((f3 - f) * PARTICLES_SIZE_MAX / (f2 - f)).toInt()
    }

    fun setValueFromInt(f: Float, f2: Float, f3: Float): Float {
        return f3 / PARTICLES_SIZE_MAX * (f2 - f) + f
    }

    fun setFromInternalPreset(settings: Settings) {
        setFrom(settings)
    }

    fun setEverythingFrom(settings: Settings) {
        setFrom(settings)
        QualityBaseValue = settings.QualityBaseValue
        GPUQuality = settings.GPUQuality
        EffectsQuality = settings.EffectsQuality
        AllowMultithreading = settings.AllowMultithreading
        FPSLimit = settings.FPSLimit
        DetailHD = settings.DetailHD
        MenuButtonVisibility = settings.MenuButtonVisibility
    }

    private fun setFrom(settings: Settings) {
        FluidType = settings.FluidType
        Force = settings.Force
        InputSize = settings.InputSize
        TouchInputForce = settings.TouchInputForce
        TouchInputSize = settings.TouchInputSize
        InputSwipeMode = settings.InputSwipeMode
        InputTouchMode = settings.InputTouchMode
        InputSwipeConstant = settings.InputSwipeConstant
        VelLifetime = settings.VelLifetime
        Swirliness = settings.Swirliness
        NumSources = settings.NumSources
        SourceSpeed = settings.SourceSpeed
        AutoOnResume = settings.AutoOnResume
        FluidAmount = settings.FluidAmount
        FluidLifeTime = settings.FluidLifeTime
        OverbrightColors = settings.OverbrightColors
        InvertColors = settings.InvertColors
        ParticlesEnabled = settings.ParticlesEnabled
        ParticlesShape = settings.ParticlesShape
        ParticlesMode = settings.ParticlesMode
        ParticlesPerSec = settings.ParticlesPerSec
        ParticlesLifeTimeSec = settings.ParticlesLifeTimeSec
        ParticlesSize = settings.ParticlesSize
        ColorChange = settings.ColorChange
        ColorOption = settings.ColorOption
        for (i in 0..5) {
            Colors[i] = settings.Colors[i]
            ColorsActive[i] = settings.ColorsActive[i]
        }
        for (i2 in 0..2) {
            DColors[i2] = settings.DColors[i2]
            DColorsActive[i2] = settings.DColorsActive[i2]
        }
        BackgroundColor = settings.BackgroundColor
        ParticlesUsePaintColor = settings.ParticlesUsePaintColor
        ParticlesColor = settings.ParticlesColor
        Gravity = settings.Gravity
        BorderMode = settings.BorderMode
        Glow = settings.Glow
        GlowLevelStrength0 = settings.GlowLevelStrength0
        GlowLevelStrength1 = settings.GlowLevelStrength1
        GlowLevelStrength2 = settings.GlowLevelStrength2
        GlowThreshold = settings.GlowThreshold
        GlowParticleIntensity = settings.GlowParticleIntensity
        LightSource = settings.LightSource
        LightRadius = settings.LightRadius
        LightIntensity = settings.LightIntensity
        LightColor = settings.LightColor
        LightSourceSpeed = settings.LightSourceSpeed
        LightSourcePosX = settings.LightSourcePosX
        LightSourcePosY = settings.LightSourcePosY
        ShadowSource = settings.ShadowSource
        ShadowSelf = settings.ShadowSelf
        ShadowInverse = settings.ShadowInverse
        ShadowIntensity = settings.ShadowIntensity
        ShadowFalloffLength = settings.ShadowFalloffLength
        UseDetailTexture = settings.UseDetailTexture
        DetailTexture = settings.DetailTexture
        DetailUVScale = settings.DetailUVScale
    }

    fun forceParticlesLifeTime() {
        val f = ParticlesPerSec
        if (ParticlesLifeTimeSec * f > 16000.0f) {
            ParticlesLifeTimeSec = Math.floor((16000.0f / f).toDouble()).toFloat()
        }
    }

    fun forceParticlesPerSec() {
        val f = ParticlesPerSec
        val f2 = ParticlesLifeTimeSec
        if (f * f2 > 16000.0f) {
            ParticlesPerSec = Math.floor((16000.0f / f2).toDouble()).toFloat()
        }
    }

    init {
        initDefault()
    }

    fun initDefault() {
        FluidType = 0
        Force = SOURCE_SPEED_MIN
        VelLifetime = 1.0f
        Swirliness = 0.0f
        NumSources = 0
        SourceSpeed = 5.0E-4f
        AutoOnResume = false
        FluidAmount = 0.0013f
        FluidLifeTime = 40.0f
        ColorOption = 0
        for (i in 0..2) {
            ColorsActive[i] = true
        }
        for (i2 in 3..5) {
            ColorsActive[i2] = false
        }
        val iArr = Colors
        iArr[0] = -48060
        iArr[1] = -12255420
        iArr[2] = -12303105
        iArr[3] = -1118482
        iArr[4] = -1118482
        iArr[5] = -1118482
        for (i3 in 0..2) {
            DColorsActive[i3] = true
        }
        val iArr2 = DColors
        iArr2[0] = -48060
        iArr2[1] = -12255420
        iArr2[2] = -12303105
        BackgroundColor = R.color.colorWhite
        OverbrightColors = true
        InvertColors = false
        ColorChange = 0
        ParticlesEnabled = false
        ParticlesMode = 0
        ParticlesShape = 0
        ParticlesPerSec = 650.0f
        ParticlesLifeTimeSec = 15.0f
        ParticlesSize = 10.0f
        ParticlesUsePaintColor = false
        ParticlesColor = -5855578
        BorderMode = 0
        Gravity = 0.0f
        Glow = false
        GlowLevelStrength0 = 0.3f
        GlowLevelStrength1 = 0.3f
        GlowLevelStrength2 = 0.3f
        GlowThreshold = 0.1f
        GlowParticleIntensity = 0.5f
        LightSource = false
        LightRadius = 1.0f
        LightIntensity = 1.0f
        LightColor = -1
        LightSourceSpeed = 0.0f
        LightSourcePosX = 0.5f
        LightSourcePosY = 0.5f
        ShadowSource = false
        ShadowSelf = true
        ShadowInverse = false
        ShadowIntensity = 3.0f
        ShadowFalloffLength = 0.5f
        UseDetailTexture = false
        DetailTexture = 0
        DetailUVScale = 2.5f
        QualityBaseValue = 1
        GPUQuality = 4
        EffectsQuality = 1
        FPSLimit = 0
        AllowMultithreading = true
        DetailHD = false
        MenuButtonVisibility = 0
    }

    fun set01ValueFromInt(f: Float): Float {
        return setValueFromInt(0.0f, 1.0f, f)
    }

    fun getIntFrom01Value(f: Float): Int {
        return getIntValue(0.0f, 1.0f, f)
    }

    var forceInt: Int
        get() = getIntValue(0.0f, FORCE_MAX, Force)
        set(i) {
            Force = setValueFromInt(0.0f, FORCE_MAX, i.toFloat())
        }
    var inputSizeInt: Int
        get() = getIntValue(0.015f, 0.125f, InputSize)
        set(i) {
            InputSize = setValueFromInt(0.015f, 0.125f, i.toFloat())
        }
    var touchInputForceInt: Int
        get() = getIntFrom01Value(TouchInputForce)
        set(i) {
            TouchInputForce = set01ValueFromInt(i.toFloat())
        }
    var touchInputSizeInt: Int
        get() = getIntFrom01Value(TouchInputSize)
        set(i) {
            TouchInputSize = set01ValueFromInt(i.toFloat())
        }
    var velLifetimeInt: Int
        get() =
            getIntValue(-1.0f, 1.0f, VelLifetime)
        set(i) {
            VelLifetime = setValueFromInt(-1.0f, 1.0f, i.toFloat())
        }
    var swirlinessInt: Int
        get() = getIntValue(0.0f, 3.0f, Swirliness)
        set(i) {
            Swirliness = setValueFromInt(0.0f, 3.0f, i.toFloat())
        }
    var fluidAmountInt: Int
        get() = getIntValue(0.0f, FLUID_AMOUNT_MAX, FluidAmount)
        set(i) {
            val valueFromInt = setValueFromInt(0.0f, FLUID_AMOUNT_MAX, i.toFloat())
            FluidAmount = valueFromInt
            if (valueFromInt < 1.0E-5f) {
                FluidAmount = 0.0f
            }
        }
    var fluidLifeTimeInt: Int
        get() = getIntValue(0.25f, FLUID_LIFE_TIME_MAX, FluidLifeTime)
        set(i) {
            FluidLifeTime = setValueFromInt(0.25f, FLUID_LIFE_TIME_MAX, i.toFloat())
        }
    var particlesPerSecInt: Int
        get() = getIntValue(50.0f, PARTICLES_PER_SEC_MAX, ParticlesPerSec)
        set(i) {
            ParticlesPerSec = setValueFromInt(50.0f, PARTICLES_PER_SEC_MAX, i.toFloat())
        }
    var particlesLifeTimeMsInt: Int
        get() = getIntValue(0.25f, PARTICLES_LIFE_TIME_MAX, ParticlesLifeTimeSec)
        set(i) {
            ParticlesLifeTimeSec = setValueFromInt(0.25f, PARTICLES_LIFE_TIME_MAX, i.toFloat())
        }
    var particlesSizeInt: Int
        get() = getIntValue(0.0f, PARTICLES_SIZE_MAX, ParticlesSize)
        set(i) {
            ParticlesSize = setValueFromInt(0.0f, PARTICLES_SIZE_MAX, i.toFloat())
        }

    fun getColor(i: Int): Int {
        return Colors[i]
    }

    fun setColor(i: Int, i2: Int) {
        Colors[i2] = i
    }

    fun getColorsActive(i: Int): Boolean {
        return ColorsActive[i]
    }

    fun setColorsActive(i: Int, z: Boolean) {
        ColorsActive[i] = z
    }

    val numColorsActive: Int
        get() {
            var i = 0
            for (i2 in 0..5) {
                if (ColorsActive[i2]) {
                    i++
                }
            }
            return i
        }

    fun getDColor(i: Int): Int {
        return DColors[i]
    }

    fun setDColor(i: Int, i2: Int) {
        DColors[i2] = i
    }

    fun getDColorsActive(i: Int): Boolean {
        return DColorsActive[i]
    }

    fun setDColorsActive(i: Int, z: Boolean) {
        DColorsActive[i] = z
    }

    val numDColorsActive: Int
        get() {
            var i = 0
            for (i2 in 0..2) {
                if (DColorsActive[i2]) {
                    i++
                }
            }
            return i
        }

    fun process() {
        var i = 0
        for (i2 in 0..5) {
            if (ColorsActive[i2]) {
                tmpColors[i] = Colors[i2]
                i++
            }
        }
        val iArr = tmpColors
        Color0 = iArr[0]
        Color1 = iArr[1]
        Color2 = iArr[2]
        Color3 = iArr[3]
        Color4 = iArr[4]
        Color5 = iArr[5]
        NumColors = i
        var i3 = 0
        for (i4 in 0..2) {
            if (DColorsActive[i4]) {
                tmpColors[i3] = DColors[i4]
                i3++
            }
        }
        val iArr2 = tmpColors
        DColor0 = iArr2[0]
        DColor1 = iArr2[1]
        DColor2 = iArr2[2]
        NumDColors = i3
        if (DetailUVScale < DETAIL_SCALE_MIN) {
            DetailUVScale = DETAIL_SCALE_MIN
        }
        if (DetailUVScale > DETAIL_SCALE_MAX) {
            DetailUVScale = DETAIL_SCALE_MAX
        }
    }

    var sourceSpeedInt: Int
        get() = getIntValue(SOURCE_SPEED_MIN, SOURCE_SPEED_MAX, SourceSpeed)
        set(i) {
            SourceSpeed = setValueFromInt(SOURCE_SPEED_MIN, SOURCE_SPEED_MAX, i.toFloat())
        }
    var gravityInt: Int
        get() = getIntValue(0.0f, GRAVITY_MAX, Gravity)
        set(i) {
            Gravity = setValueFromInt(0.0f, GRAVITY_MAX, i.toFloat())
        }
    var glowLevelStrength0Int: Int
        get() = getIntValue(0.0f, DETAIL_SCALE_MIN, GlowLevelStrength0)
        set(i) {
            GlowLevelStrength0 = setValueFromInt(0.0f, DETAIL_SCALE_MIN, i.toFloat())
        }
    var glowLevelStrength1Int: Int
        get() = getIntValue(0.0f, DETAIL_SCALE_MIN, GlowLevelStrength1)
        set(i) {
            GlowLevelStrength1 = setValueFromInt(0.0f, DETAIL_SCALE_MIN, i.toFloat())
        }
    var glowLevelStrength2Int: Int
        get() = getIntValue(0.0f, DETAIL_SCALE_MIN, GlowLevelStrength2)
        set(i) {
            GlowLevelStrength2 = setValueFromInt(0.0f, DETAIL_SCALE_MIN, i.toFloat())
        }
    var glowThresholdInt: Int
        get() = getIntValue(0.0f, DETAIL_SCALE_MIN, GlowThreshold)
        set(i) {
            GlowThreshold = setValueFromInt(0.0f, DETAIL_SCALE_MIN, i.toFloat())
        }
    var lightRadiusInt: Int
        get() = getIntValue(0.15f, 2.0f, LightRadius)
        set(i) {
            LightRadius = setValueFromInt(0.15f, 2.0f, i.toFloat())
        }
    var lightIntensityInt: Int
        get() = getIntValue(0.25f, 2.0f, LightIntensity)
        set(i) {
            LightIntensity = setValueFromInt(0.25f, 2.0f, i.toFloat())
        }
    var lightSourceSpeedInt: Int
        get() = getIntValue(0.0f, 2.0f, LightSourceSpeed)
        set(i) {
            LightSourceSpeed = setValueFromInt(0.0f, 2.0f, i.toFloat())
        }
    var lightSourcePosXInt: Int
        get() = getIntValue(-0.1f, 1.1f, LightSourcePosX)
        set(i) {
            LightSourcePosX = setValueFromInt(-0.1f, 1.1f, i.toFloat())
        }
    var lightSourcePosYInt: Int
        get() = getIntValue(-0.1f, 1.1f, LightSourcePosY)
        set(i) {
            LightSourcePosY = setValueFromInt(-0.1f, 1.1f, i.toFloat())
        }
    var shadowIntensityInt: Int
        get() = getIntValue(0.5f, 8.0f, ShadowIntensity)
        set(i) {
            ShadowIntensity = setValueFromInt(0.5f, 8.0f, i.toFloat())
        }
    var shadowFalloffLengthInt: Int
        get() = getIntValue(0.025f, 1.0f, ShadowFalloffLength)
        set(i) {
            ShadowFalloffLength = setValueFromInt(0.025f, 1.0f, i.toFloat())
        }
    var detailUVScaleInt: Int
        get() = getIntValue(DETAIL_SCALE_MIN, DETAIL_SCALE_MAX, DetailUVScale)
        set(i) {
            DetailUVScale = setValueFromInt(DETAIL_SCALE_MIN, DETAIL_SCALE_MAX, i.toFloat())
        }

    companion object {

        const val BORDER_WRAP = 1
        const val BORDER_WRAP_MIRROR = 2
        const val COLOR_CHG_TIME = 1
        const val COLOR_CHG_TOUCH = 0
        const val COLOR_DOUBLE_PALETTE = 2
        const val COLOR_PALETTE = 1
        const val COLOR_RANDOM = 0
        const val COLOR_TRIPPY = 3
        const val DETAIL_SCALE_MAX = 3.5f
        const val DETAIL_SCALE_MIN = 1.5f
        const val FLUID_AMOUNT_MAX = 0.005f
        const val FLUID_AMOUNT_MIN = 0.0f
        const val FLUID_LIFE_TIME_MAX = 51.0f
        const val FLUID_LIFE_TIME_MIN = 0.25f
        const val FLUID_TYPE_JELLO = 2
        const val FLUID_TYPE_SMOKE = 0
        const val FLUID_TYPE_WATER = 1
        const val FORCE_MAX = 0.001f
        const val FORCE_MIN = 0.0f
        const val GRAVITY_ENABLE_THRESHOLD = 3.0E-4f
        const val GRAVITY_MAX = 0.012f
        const val GRAVITY_MIN = 0.0f
        const val MENU_BUTTON_DIMMED = 1
        const val MENU_BUTTON_HIDDEN = 2
        const val MENU_BUTTON_VISIBLE = 0
        const val NUM_COLORS = 6
        const val NUM_DCOLORS = 3
        const val PARTICLES_LIFE_TIME_MAX = 30.0f
        const val PARTICLES_LIFE_TIME_MIN = 0.25f
        const val PARTICLES_PER_SEC_MAX = 2000.0f
        const val PARTICLES_PER_SEC_MIN = 50.0f
        const val PARTICLES_SIZE_MAX = 100.0f
        const val PARTICLES_SIZE_MIN = 0.0f
        const val PARTICLE_MODE_FILL = 1
        const val PARTICLE_MODE_REGULAR = 0
        const val PARTICLE_SHAPE_DOTS = 0
        const val PARTICLE_SHAPE_LINES = 1
        const val PARTICLE_SHAPE_STARS = 2
        const val SOURCE_SPEED_MAX = 0.0015f
        const val SOURCE_SPEED_MIN = 1.5E-4f
        @JvmField
        var Current: Settings? = null
    }
}