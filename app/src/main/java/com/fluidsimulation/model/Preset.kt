package com.fluidsimulation.model

import android.annotation.*
import android.app.*
import android.content.*
import android.content.res.*
import android.graphics.*
import androidx.core.internal.view.*
import androidx.core.view.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.helper.*
import com.magicfluids.*
import java.io.*

private const val TAG = "Preset"

enum class Status {
    FREE, LOCKED, UN_LOCKABLE
}
@JvmField
var presetList: MutableList<PresetNew>? = mutableListOf()

data class PresetNew(var setting: Settings, var name: String, var status: Status, var isNew: Boolean)

fun Context.addPreset(name: String, status: Status, isNew: Boolean) {
    val settings = Settings()
    val preset = PresetNew(settings, name, status, isNew)
    loadSettingsFromAssets(settings, "presets/" + preset.name.replace(" ", ""), isNew)
    presetList?.add(preset)
}

private var map: MutableMap<String, String> = mutableMapOf()

fun Context.initPresets() {
    addPreset("Gleeful Glimmers", Status.FREE, false)
    addPreset("Flashy Fluids", Status.FREE, false)
    addPreset("Blinding Bliss", Status.FREE, false)
    addPreset("Life of Lights", Status.FREE, false)
    addPreset("Cosmic Charm", Status.FREE, false)
    addPreset("Strange Substance", Status.FREE, false)
    addPreset("Jittery Jello", Status.FREE, false)
    addPreset("Radioactive Rumble", Status.FREE, false)
    addPreset("Wizard Wand", Status.FREE, false)
    addPreset("Dancing in the Dark", Status.FREE, false)
    addPreset("Blinking Beauty", Status.FREE, false)
    addPreset("Earthly Elements", Status.FREE, false)
    addPreset("Crazy Colors", Status.FREE, false)
    addPreset("Random Remarkability", Status.FREE, false)
    addPreset("Gravity Game", Status.FREE, false)
    addPreset("Wonderful Whirls", Status.FREE, false)
    addPreset("Fading Forms", Status.FREE, false)
    addPreset("Wavy Winter", Status.FREE, false)
    addPreset("Bloody Blue", Status.FREE, false)
    addPreset("Lake of Lava", Status.FREE, false)
    addPreset("Steady Sea", Status.FREE, false)
    addPreset("Freaky Fun", Status.FREE, false)
    addPreset("Incredible Ink", Status.FREE, false)
    addPreset("Gentle Glow", Status.FREE, false)
    addPreset("Transient Thoughts", Status.FREE, false)
    addPreset("Glorious Galaxies", Status.FREE, false)
    addPreset("Something Strange", Status.FREE, false)
    addPreset("Cloudy Composition", Status.FREE, false)
    addPreset("Glowing Glare", Status.FREE, false)
    addPreset("Trippy Tint", Status.FREE, false)
    addPreset("Girls Game", Status.FREE, false)
    addPreset("Particle Party", Status.FREE, false)
    addPreset("Busy Brilliance", Status.FREE, false)
    addPreset("Grainy Greatness", Status.FREE, false)
    addPreset("Magic Trail by Toni", Status.FREE, false)
    addPreset("Lovely Liquid", Status.FREE, false)
    addPreset("Floating Flames", Status.FREE, false)
    addPreset("Glimming Glow", Status.FREE, false)
    addPreset("Subtle Setting", Status.FREE, false)
    addPreset("Calm Christmas", Status.FREE, false)
    addPreset("Bouncing Beach", Status.FREE, false)
    addPreset("Classy Combination", Status.FREE, false)
    addPreset("Swirly Sparkles", Status.FREE, false)
}

fun Context.readFromFileAssets(path: String): String {
    var ret = ""
    try {
        val inputStream: InputStream = assets.open(path)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var receiveString: String? = ""
        val stringBuilder = StringBuilder()
        while (bufferedReader.readLine().also { receiveString = it } != null) {
            stringBuilder.append(receiveString)
        }
        inputStream.close()
        ret = stringBuilder.toString()
    } catch (e: FileNotFoundException) {
        "FileNotFoundException".log("File not found: $e")
    } catch (e: IOException) {
        "IOException".log("Can not read file: $e")
    }
    return ret
}

fun Context.loadSettingsFromAssets(setting: Settings, assetsPath: String, isNew: Boolean) {
    val split: List<String> = readFromFileAssets(assetsPath).split("\\s+".toRegex()).filter { it.isNotEmpty() }
    if ((split?.size ?: 0) % 2 != 0) {
        TAG.log("Settings file $assetsPath has incorrect format")
        return
    }

    var i = 0
    split?.let {
        while (i < it.size) {
            map.put(it[i], it[i + 1])
            i += 2
        }
    }

    setting.FluidType = getInt(FLUID_TYPE, 0)
    setting.Force = getFloat(FORCE, 3.0E-4f)
    setting.InputSize = getFloat(INPUT_SIZE, 0.03f)
    setting.TouchInputForce = getFloat(TOUCH_INPUT_FORCE, 0.2f)
    setting.TouchInputSize = getFloat(TOUCH_INPUT_SIZE, 0.25f)
    setting.InputSwipeMode = getInt(INPUT_SWIPE_MODE, 0)
    setting.InputTouchMode = getInt(INPUT_TOUCH_MODE, 4)
    setting.InputSwipeConstant = getBoolean(INPUT_SWIPE_CONSTANT, false)
    setting.VelLifetime = getFloat(VEL_LIFE_TIME, 1.0f)
    setting.Swirliness = getFloat(SWIRLINESS, 0.0f)
    setting.NumSources = getInt(NUM_SOURCES, 0)
    setting.SourceSpeed = getFloat(SOURCE_SPEED, 6.0E-4f)
    setting.AutoOnResume = getBoolean(AUTO_ON_RESUME, false)
    setting.FluidAmount = getFloat(FLUID_AMOUNT, 0.002f)
    setting.FluidLifeTime = getFloat(FLUID_LIFE_TIME, 5.0f)
    setting.ColorChange = getInt(COLOR_CHANGE, 0)
    setting.ColorOption = getInt(COLOR_OPTION, 1)
    setting.Colors.set(0, getInt(COLOR0, SupportMenu.CATEGORY_MASK))
    setting.Colors.set(1, getInt(COLOR1, -16711936))
    setting.Colors.set(2, getInt(COLOR2, -16776961))
    setting.Colors.set(3, getInt(COLOR3, -1))
    setting.Colors.set(4, getInt(COLOR4, -1))
    setting.Colors.set(5, getInt(COLOR5, -1))
    setting.ColorsActive.set(0, getBoolean(COLOR_ACTIVE0, true))
    setting.ColorsActive.set(1, getBoolean(COLOR_ACTIVE1, true))
    setting.ColorsActive.set(2, getBoolean(COLOR_ACTIVE2, true))
    setting.ColorsActive.set(3, getBoolean(COLOR_ACTIVE3, false))
    setting.ColorsActive.set(4, getBoolean(COLOR_ACTIVE4, false))
    setting.ColorsActive.set(5, getBoolean(COLOR_ACTIVE5, false))
    setting.DColors.set(0, getInt(DCOLOR0, SupportMenu.CATEGORY_MASK))
    setting.DColors.set(1, getInt(DCOLOR1, -16711936))
    setting.DColors.set(2, getInt(DCOLOR2, -16711936))
    setting.DColorsActive.set(0, getBoolean(DCOLOR_ACTIVE0, true))
    setting.DColorsActive.set(1, getBoolean(DCOLOR_ACTIVE1, true))
    setting.DColorsActive.set(2, getBoolean(DCOLOR_ACTIVE2, true))
    setting.BackgroundColor = getInt(BACKGROUND_COLOR, -1)
    setting.OverbrightColors = getBoolean(OVERBRIGHT_COLORS, true)
    setting.InvertColors = getBoolean(INVERT_COLORS, false)
    setting.ParticlesEnabled = getBoolean(PARTICLES_ENABLED, false)
    setting.ParticlesMode = getInt(PARTICLES_MODE, 0)
    setting.ParticlesShape = getInt(PARTICLES_SHAPE, 0)
    setting.ParticlesPerSec = getFloat(PARTICLES_PER_SEC, 1000.0f)
    setting.ParticlesLifeTimeSec = getFloat(PARTICLES_LIFE_TIME_SEC, 5.0f)
    setting.ParticlesSize = getFloat(PARTICLES_SIZE, 15.0f)
    setting.ParticlesColor = getInt(PARTICLES_COLOR, -3355444)
    setting.ParticlesUsePaintColor = getBoolean(PARTICLES_USE_PAINT_COLOR, true)
    setting.BorderMode = getInt(BORDER_MODE, 0)
    setting.Gravity = getFloat(GRAVITY, 0.0f)
    setting.Glow = getBoolean(GLOW, false)
    setting.GlowLevelStrength0 = getFloat(GLOW_LEVEL_STRENGTH0, 0.3f)
    setting.GlowLevelStrength1 = getFloat(GLOW_LEVEL_STRENGTH1, 0.3f)
    setting.GlowLevelStrength2 = getFloat(GLOW_LEVEL_STRENGTH2, 0.3f)
    setting.GlowThreshold = getFloat(GLOW_THRESHOLD, 0.0f)
    setting.GlowParticleIntensity = getFloat(GLOW_PARTICLE_INTENSITY, 0.0f)
    setting.LightSource = getBoolean(LIGHT_SOURCE, false)
    setting.LightRadius = getFloat(LIGHT_RADIUS, 1.0f)
    setting.LightIntensity = getFloat(LIGHT_INTENSITY, 1.0f)
    setting.LightColor = getInt(LIGHT_COLOR, -1)
    setting.LightSourceSpeed = getFloat(LIGHT_SOURCE_SPEED, 0.0f)
    setting.LightSourcePosX = getFloat(LIGHT_SOURCE_POSX, 0.5f)
    setting.LightSourcePosY = getFloat(LIGHT_SOURCE_POSY, 0.5f)
    setting.ShadowSource = getBoolean(SHADOW_SOURCE, false)
    setting.ShadowSelf = getBoolean(SHADOW_SELF, true)
    setting.ShadowInverse = getBoolean(SHADOW_INVERSE, false)
    setting.ShadowIntensity = getFloat(SHADOW_INTENSITY, 3.0f)
    setting.ShadowFalloffLength = getFloat(SHADOW_FALLOFF_LENGTH, 0.5f)
    setting.UseDetailTexture = getBoolean(USE_DETAIL_TEXTURE, false)
    setting.DetailTexture = getInt(DETAIL_TEXTURE, 0)
    setting.DetailUVScale = getFloat(DETAIL_UV_SCALE, 2.5f)
    if (isNew) {
        setting.QualityBaseValue = getInt(QUALITY_BASE_VALUE, 1)
        setting.GPUQuality = getInt(GPU_ANIMATION, 2)
        setting.EffectsQuality = getInt(EFFECTS_QUALITY, 1)
        setting.MenuButtonVisibility = getInt(MENU_BUTTON_VISIBILITY, 0)
        if (setting.QualityBaseValue > 10) {
            setting.MenuButtonVisibility = 0
            val i: Int = setting.QualityBaseValue
            val i2: Int = setting.GPUQuality
            if (i <= 208) {
                setting.QualityBaseValue = 0
                setting.EffectsQuality = 0
            } else {
                setting.QualityBaseValue = 1
            }
            if (i2 == 0) {
                setting.GPUQuality = 0
            } else if (i2 == 1 && i <= 208) {
                setting.GPUQuality = 1
            } else if ((i2 != 1 || i > 384) && i > 208) {
                setting.EffectsQuality = 2
                if (i2 == 2 && i == 576) {
                    setting.GPUQuality = 4
                    setting.EffectsQuality = 3
                } else {
                    setting.GPUQuality = 3
                    setting.EffectsQuality = 2
                }
            } else {
                setting.GPUQuality = 2
            }
        }
        setting.FPSLimit = getInt(FPS_LIMIT, 0)
        setting.AllowMultithreading = getBoolean(ALLOW_MULTITHREADING, false)
        setting.DetailHD = getBoolean(DETAIL_HD, false)
    }
    for (i3 in 0..5) {
        val iArr: IntArray = setting.Colors
        iArr[i3] = -16777216 or iArr[i3]
    }
    for (i4 in 0..2) {
        val iArr2: IntArray = setting.DColors
        iArr2[i4] = iArr2[i4] or ViewCompat.MEASURED_STATE_MASK
    }
    setting.BackgroundColor = setting.BackgroundColor or ViewCompat.MEASURED_STATE_MASK
    setting.ParticlesColor = setting.ParticlesColor or ViewCompat.MEASURED_STATE_MASK
    setting.LightColor = setting.LightColor or ViewCompat.MEASURED_STATE_MASK
}

fun getFloat(str: String?, f: Float): Float {
    val key: String? = map.get(str)
    return java.lang.Float.valueOf(key) ?: f
}

fun getInt(str: String?, i: Int): Int {
    val key: String? = map.get(str)
    return Integer.valueOf(key) ?: i
}

fun getBoolean(str: String?, z: Boolean): Boolean {
    val key: String = map.get(str).toString()
    return java.lang.Boolean.valueOf(key) ?: z
}

@SuppressLint("RestrictedApi")
fun Context.loadSettingsFromMap(setting: Settings, pref: String, isNew: Boolean) {
    val tinyDB = TinyDB(this, pref)
    setting.FluidType = tinyDB.getInt(FLUID_TYPE, 1)
    setting.Force = tinyDB.getFloat(FORCE, 3.0E-4f)
    setting.InputSize = tinyDB.getFloat(INPUT_SIZE, 0.03f)
    setting.TouchInputForce = tinyDB.getFloat(TOUCH_INPUT_FORCE, 0.2f)
    setting.TouchInputSize = tinyDB.getFloat(TOUCH_INPUT_SIZE, 0.25f)
    setting.InputSwipeMode = tinyDB.getInt(INPUT_SWIPE_MODE, 0)
    setting.InputTouchMode = tinyDB.getInt(INPUT_TOUCH_MODE, 4)
    setting.InputSwipeConstant = tinyDB.getBoolean(INPUT_SWIPE_CONSTANT, false)
    setting.VelLifetime = tinyDB.getFloat(VEL_LIFE_TIME, 1.0f)
    setting.Swirliness = tinyDB.getFloat(SWIRLINESS, 0.0f)
    setting.NumSources = tinyDB.getInt(NUM_SOURCES, 0)
    setting.SourceSpeed = tinyDB.getFloat(SOURCE_SPEED, 6.0E-4f)
    setting.AutoOnResume = tinyDB.getBoolean(AUTO_ON_RESUME, false)
    setting.FluidAmount = tinyDB.getFloat(FLUID_AMOUNT, 0.002f)
    setting.FluidLifeTime = tinyDB.getFloat(FLUID_LIFE_TIME, 5.0f)
    setting.ColorChange = tinyDB.getInt(COLOR_CHANGE, 0)
    setting.ColorOption = tinyDB.getInt(COLOR_OPTION, 1)

    setting.Colors[0] = tinyDB.getInt(COLOR0, SupportMenu.CATEGORY_MASK)
    setting.Colors[1] = tinyDB.getInt(COLOR1, -16711936)
    setting.Colors[2] = tinyDB.getInt(COLOR2, -16776961)
    setting.Colors[3] = tinyDB.getInt(COLOR3, -1)
    setting.Colors[4] = tinyDB.getInt(COLOR4, -1)
    setting.Colors[5] = tinyDB.getInt(COLOR5, -1)
    setting.ColorsActive[0] = tinyDB.getBoolean(COLOR_ACTIVE0, true)
    setting.ColorsActive[1] = tinyDB.getBoolean(COLOR_ACTIVE1, true)
    setting.ColorsActive[2] = tinyDB.getBoolean(COLOR_ACTIVE2, true)
    setting.ColorsActive[3] = tinyDB.getBoolean(COLOR_ACTIVE3, false)
    setting.ColorsActive[4] = tinyDB.getBoolean(COLOR_ACTIVE4, false)
    setting.ColorsActive[5] = tinyDB.getBoolean(COLOR_ACTIVE5, false)
    setting.DColors[0] = tinyDB.getInt(DCOLOR0, SupportMenu.CATEGORY_MASK)
    setting.DColors[1] = tinyDB.getInt(DCOLOR1, -16711936)
    setting.DColors[2] = tinyDB.getInt(DCOLOR2, -16711936)
    setting.DColorsActive[0] = tinyDB.getBoolean(DCOLOR_ACTIVE0, true)
    setting.DColorsActive[1] = tinyDB.getBoolean(DCOLOR_ACTIVE1, true)
    setting.DColorsActive[2] = tinyDB.getBoolean(DCOLOR_ACTIVE2, true)
    setting.BackgroundColor = tinyDB.getInt(BACKGROUND_COLOR, -1)
    setting.OverbrightColors = tinyDB.getBoolean(OVERBRIGHT_COLORS, true)
    setting.InvertColors = tinyDB.getBoolean(INVERT_COLORS, false)
    setting.ParticlesEnabled = tinyDB.getBoolean(PARTICLES_ENABLED, false)
    setting.ParticlesMode = tinyDB.getInt(PARTICLES_MODE, 0)
    setting.ParticlesShape = tinyDB.getInt(PARTICLES_SHAPE, 0)
    setting.ParticlesPerSec = tinyDB.getFloat(PARTICLES_PER_SEC, 1000.0f)
    setting.ParticlesLifeTimeSec = tinyDB.getFloat(PARTICLES_LIFE_TIME_SEC, 5.0f)
    setting.ParticlesSize = tinyDB.getFloat(PARTICLES_SIZE, 15.0f)
    setting.ParticlesColor = tinyDB.getInt(PARTICLES_COLOR, -3355444)
    setting.ParticlesUsePaintColor = tinyDB.getBoolean(PARTICLES_USE_PAINT_COLOR, true)
    setting.BorderMode = tinyDB.getInt(BORDER_MODE, 0)
    setting.Gravity = tinyDB.getFloat(GRAVITY, 0.0f)
    setting.Glow = tinyDB.getBoolean(GLOW, false)
    setting.GlowLevelStrength0 = tinyDB.getFloat(GLOW_LEVEL_STRENGTH0, 0.3f)
    setting.GlowLevelStrength1 = tinyDB.getFloat(GLOW_LEVEL_STRENGTH1, 0.3f)
    setting.GlowLevelStrength2 = tinyDB.getFloat(GLOW_LEVEL_STRENGTH2, 0.3f)
    setting.GlowThreshold = tinyDB.getFloat(GLOW_THRESHOLD, 0.0f)
    setting.GlowParticleIntensity = tinyDB.getFloat(GLOW_PARTICLE_INTENSITY, 0.0f)
    setting.LightSource = tinyDB.getBoolean(LIGHT_SOURCE, false)
    setting.LightRadius = tinyDB.getFloat(LIGHT_RADIUS, 1.0f)
    setting.LightIntensity = tinyDB.getFloat(LIGHT_INTENSITY, 1.0f)
    setting.LightColor = tinyDB.getInt(LIGHT_COLOR, -1)
    setting.LightSourceSpeed = tinyDB.getFloat(LIGHT_SOURCE_SPEED, 0.0f)
    setting.LightSourcePosX = tinyDB.getFloat(LIGHT_SOURCE_POSX, 0.5f)
    setting.LightSourcePosY = tinyDB.getFloat(LIGHT_SOURCE_POSY, 0.5f)
    setting.ShadowSource = tinyDB.getBoolean(SHADOW_SOURCE, false)
    setting.ShadowSelf = tinyDB.getBoolean(SHADOW_SELF, true)
    setting.ShadowInverse = tinyDB.getBoolean(SHADOW_INVERSE, false)
    setting.ShadowIntensity = tinyDB.getFloat(SHADOW_INTENSITY, 3.0f)
    setting.ShadowFalloffLength = tinyDB.getFloat(SHADOW_FALLOFF_LENGTH, 0.5f)
    setting.UseDetailTexture = tinyDB.getBoolean(USE_DETAIL_TEXTURE, false)
    setting.DetailTexture = tinyDB.getInt(DETAIL_TEXTURE, 0)
    setting.DetailUVScale = tinyDB.getFloat(DETAIL_UV_SCALE, 2.5f)
    if (isNew) {
        setting.QualityBaseValue = tinyDB.getInt(QUALITY_BASE_VALUE, 1)
        setting.GPUQuality = tinyDB.getInt(GPU_ANIMATION, 2)
        setting.EffectsQuality = tinyDB.getInt(EFFECTS_QUALITY, 1)
        setting.MenuButtonVisibility = tinyDB.getInt(MENU_BUTTON_VISIBILITY, 0)
        if (setting.QualityBaseValue > 10) {
            setting.MenuButtonVisibility = 0
            val i = setting.QualityBaseValue
            val i2 = setting.GPUQuality
            if (i <= 208) {
                setting.QualityBaseValue = 0
                setting.EffectsQuality = 0
            } else {
                setting.QualityBaseValue = 1
            }
            if (i2 == 0) {
                setting.GPUQuality = 0
            } else if (i2 == 1 && i <= 208) {
                setting.GPUQuality = 1
            } else if ((i2 != 1 || i > 384) && i > 208) {
                setting.EffectsQuality = 2
                if (i2 == 2 && i == 576) {
                    setting.GPUQuality = 4
                    setting.EffectsQuality = 3
                } else {
                    setting.GPUQuality = 3
                    setting.EffectsQuality = 2
                }
            } else {
                setting.GPUQuality = 2
            }
        }
        setting.FPSLimit = tinyDB.getInt(FPS_LIMIT, 0)
        setting.AllowMultithreading = tinyDB.getBoolean(ALLOW_MULTITHREADING, false)
        setting.DetailHD = tinyDB.getBoolean(DETAIL_HD, false)
    }
    for (i3 in 0..5) {
        val iArr = setting.Colors
        iArr[i3] = -16777216 or iArr[i3]
    }
    for (i4 in 0..2) {
        val iArr2 = setting.DColors
        iArr2[i4] = iArr2[i4] or ViewCompat.MEASURED_STATE_MASK
    }
    setting.BackgroundColor = setting.BackgroundColor or ViewCompat.MEASURED_STATE_MASK
    setting.ParticlesColor = setting.ParticlesColor or ViewCompat.MEASURED_STATE_MASK
    setting.LightColor = setting.LightColor or ViewCompat.MEASURED_STATE_MASK
}

fun Activity.saveSessionSettings(settings: Settings, str: String) {
    saveSettings(settings, str)
}

fun Activity.saveSettings(setting: Settings, pref: String) {
    val tinyDB = TinyDB(this, pref)
    tinyDB.putInt(FLUID_TYPE, setting.FluidType)
    tinyDB.putFloat(FORCE, setting.Force)
    tinyDB.putFloat(INPUT_SIZE, setting.InputSize)
    tinyDB.putFloat(TOUCH_INPUT_FORCE, setting.TouchInputForce)
    tinyDB.putFloat(TOUCH_INPUT_SIZE, setting.TouchInputSize)
    tinyDB.putInt(INPUT_SWIPE_MODE, setting.InputSwipeMode)
    tinyDB.putInt(INPUT_TOUCH_MODE, setting.InputTouchMode)
    tinyDB.putBoolean(INPUT_SWIPE_CONSTANT, setting.InputSwipeConstant)
    tinyDB.putFloat(VEL_LIFE_TIME, setting.VelLifetime)
    tinyDB.putFloat(SWIRLINESS, setting.Swirliness)
    tinyDB.putInt(NUM_SOURCES, setting.NumSources)
    tinyDB.putFloat(SOURCE_SPEED, setting.SourceSpeed)
    tinyDB.putBoolean(AUTO_ON_RESUME, setting.AutoOnResume)
    tinyDB.putInt(FPS_LIMIT, setting.FPSLimit)
    tinyDB.putInt(GPU_ANIMATION, setting.GPUQuality)
    tinyDB.putBoolean(ALLOW_MULTITHREADING, setting.AllowMultithreading)
    tinyDB.putFloat(FLUID_AMOUNT, setting.FluidAmount)
    tinyDB.putFloat(FLUID_LIFE_TIME, setting.FluidLifeTime)
    tinyDB.putInt(COLOR_CHANGE, setting.ColorChange)
    tinyDB.putInt(COLOR_OPTION, setting.ColorOption)
    tinyDB.putInt(COLOR0, setting.Colors.get(0))
    tinyDB.putInt(COLOR1, setting.Colors.get(1))
    tinyDB.putInt(COLOR2, setting.Colors.get(2))
    tinyDB.putInt(COLOR3, setting.Colors.get(3))
    tinyDB.putInt(COLOR4, setting.Colors.get(4))
    tinyDB.putInt(COLOR5, setting.Colors.get(5))
    tinyDB.putBoolean(COLOR_ACTIVE0, setting.ColorsActive.get(0))
    tinyDB.putBoolean(COLOR_ACTIVE1, setting.ColorsActive.get(1))
    tinyDB.putBoolean(COLOR_ACTIVE2, setting.ColorsActive.get(2))
    tinyDB.putBoolean(COLOR_ACTIVE3, setting.ColorsActive.get(3))
    tinyDB.putBoolean(COLOR_ACTIVE4, setting.ColorsActive.get(4))
    tinyDB.putBoolean(COLOR_ACTIVE5, setting.ColorsActive.get(5))
    tinyDB.putInt(DCOLOR0, setting.DColors.get(0))
    tinyDB.putInt(DCOLOR1, setting.DColors.get(1))
    tinyDB.putInt(DCOLOR2, setting.DColors.get(2))
    tinyDB.putBoolean(DCOLOR_ACTIVE0, setting.DColorsActive.get(0))
    tinyDB.putBoolean(DCOLOR_ACTIVE1, setting.DColorsActive.get(1))
    tinyDB.putBoolean(DCOLOR_ACTIVE2, setting.DColorsActive.get(2))
    tinyDB.putInt(BACKGROUND_COLOR, setting.BackgroundColor)
    tinyDB.putBoolean(OVERBRIGHT_COLORS, setting.OverbrightColors)
    tinyDB.putBoolean(INVERT_COLORS, setting.InvertColors)
    tinyDB.putBoolean(PARTICLES_ENABLED, setting.ParticlesEnabled)
    tinyDB.putInt(PARTICLES_MODE, setting.ParticlesMode)
    tinyDB.putInt(PARTICLES_SHAPE, setting.ParticlesShape)
    tinyDB.putFloat(PARTICLES_PER_SEC, setting.ParticlesPerSec)
    tinyDB.putFloat(PARTICLES_LIFE_TIME_SEC, setting.ParticlesLifeTimeSec)
    tinyDB.putFloat(PARTICLES_SIZE, setting.ParticlesSize)
    tinyDB.putInt(PARTICLES_COLOR, setting.ParticlesColor)
    tinyDB.putBoolean(PARTICLES_USE_PAINT_COLOR, setting.ParticlesUsePaintColor)
    tinyDB.putInt(BORDER_MODE, setting.BorderMode)
    tinyDB.putFloat(GRAVITY, setting.Gravity)
    tinyDB.putBoolean(GLOW, setting.Glow)
    tinyDB.putFloat(GLOW_LEVEL_STRENGTH0, setting.GlowLevelStrength0)
    tinyDB.putFloat(GLOW_LEVEL_STRENGTH1, setting.GlowLevelStrength1)
    tinyDB.putFloat(GLOW_LEVEL_STRENGTH2, setting.GlowLevelStrength2)
    tinyDB.putFloat(GLOW_THRESHOLD, setting.GlowThreshold)
    tinyDB.putFloat(GLOW_PARTICLE_INTENSITY, setting.GlowParticleIntensity)
    tinyDB.putBoolean(LIGHT_SOURCE, setting.LightSource)
    tinyDB.putFloat(LIGHT_RADIUS, setting.LightRadius)
    tinyDB.putFloat(LIGHT_INTENSITY, setting.LightIntensity)
    tinyDB.putInt(LIGHT_COLOR, setting.LightColor)
    tinyDB.putFloat(LIGHT_SOURCE_SPEED, setting.LightSourceSpeed)
    tinyDB.putFloat(LIGHT_SOURCE_POSX, setting.LightSourcePosX)
    tinyDB.putFloat(LIGHT_SOURCE_POSY, setting.LightSourcePosY)
    tinyDB.putBoolean(SHADOW_SOURCE, setting.ShadowSource)
    tinyDB.putBoolean(SHADOW_SELF, setting.ShadowSelf)
    tinyDB.putBoolean(SHADOW_INVERSE, setting.ShadowInverse)
    tinyDB.putFloat(SHADOW_INTENSITY, setting.ShadowIntensity)
    tinyDB.putFloat(SHADOW_FALLOFF_LENGTH, setting.ShadowFalloffLength)
    tinyDB.putBoolean(USE_DETAIL_TEXTURE, setting.UseDetailTexture)
    tinyDB.putInt(DETAIL_TEXTURE, setting.DetailTexture)
    tinyDB.putFloat(DETAIL_UV_SCALE, setting.DetailUVScale)
    tinyDB.putBoolean(DETAIL_HD, setting.DetailHD)
    tinyDB.putInt(QUALITY_BASE_VALUE, setting.QualityBaseValue)
    tinyDB.putInt(EFFECTS_QUALITY, setting.EffectsQuality)
    tinyDB.putInt(MENU_BUTTON_VISIBILITY, setting.MenuButtonVisibility)
}