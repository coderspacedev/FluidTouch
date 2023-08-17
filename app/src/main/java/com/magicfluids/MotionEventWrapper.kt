package com.magicfluids

class MotionEventWrapper {

    var ID = 0
    var PosX = 0f
    var PosY = 0f
    var Type = 0
    fun set(motionEventWrapper: MotionEventWrapper) {
        Type = motionEventWrapper.Type
        PosX = motionEventWrapper.PosX
        PosY = motionEventWrapper.PosY
        ID = motionEventWrapper.ID
    }

    companion object {
        const val EVENT_DOWN = 0
        const val EVENT_MOVE = 2
        const val EVENT_POINTER_DOWN = 5
        const val EVENT_POINTER_UP = 6
        const val EVENT_UP = 1
    }
}