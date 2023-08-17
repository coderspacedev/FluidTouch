package com.magicfluids

class Input {

    var Events = arrayOfNulls<MotionEventWrapper>(1024)
    var NumEvents: Int

    init {
        for (i in 0..1023) {
            Events[i] = MotionEventWrapper()
        }
        NumEvents = 0
    }
}