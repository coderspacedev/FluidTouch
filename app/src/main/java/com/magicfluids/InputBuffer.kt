package com.magicfluids

import android.view.*
import java.util.concurrent.*

class InputBuffer {

    private var eventPool: ArrayBlockingQueue<MotionEventWrapper>? = null
    private var newEvents: ArrayBlockingQueue<MotionEventWrapper>? = null

    init {
        init()
    }

    fun init() {
        eventPool = ArrayBlockingQueue(1024)
        newEvents = ArrayBlockingQueue(1024)
        for (i in 0..1023) {
            eventPool?.add(MotionEventWrapper())
        }
    }

    fun addNewEvent(i: Int, i2: Int, f: Float, f2: Float): Boolean {
        val poll = eventPool?.poll() ?: return false
        poll.Type = i
        poll.PosX = f
        poll.PosY = f2
        poll.ID = i2
        newEvents?.add(poll)
        return true
    }

    fun addEvent(motionEvent: MotionEvent) {
        val actionMasked = motionEvent.actionMasked
        if (actionMasked == 0) {
            addNewEvent(0, motionEvent.getPointerId(0), motionEvent.x, motionEvent.y)
        } else if (actionMasked == 1 || actionMasked == 3) {
            addNewEvent(1, motionEvent.getPointerId(0), motionEvent.x, motionEvent.y)
        } else {
            if (actionMasked == 5) {
                val actionIndex = motionEvent.actionIndex
                if (!addNewEvent(5, motionEvent.getPointerId(actionIndex), motionEvent.getX(actionIndex), motionEvent.getY(actionIndex))) {
                    return
                }
            }
            if (actionMasked == 6) {
                val actionIndex2 = motionEvent.actionIndex
                if (!addNewEvent(6, motionEvent.getPointerId(actionIndex2), motionEvent.getX(actionIndex2), motionEvent.getY(actionIndex2))) {
                    return
                }
            }
            if (actionMasked == 2) {
                var i = 0
                while (i < motionEvent.pointerCount && addNewEvent(2, motionEvent.getPointerId(i), motionEvent.getX(i), motionEvent.getY(i))) {
                    i++
                }
            }
        }
    }

    fun getCurrentInputState(input: Input) {
        input.NumEvents = 0
        while (newEvents?.isEmpty()?.not() == true) {
            val poll = newEvents?.poll()
            if (poll != null) {
                val motionEventWrapperArr = input.Events
                val i = input.NumEvents
                input.NumEvents = i + 1
                motionEventWrapperArr[i]?.set(poll)
                eventPool?.add(poll)
            }
        }
    }

    companion object {
        @JvmField
        var Instance = InputBuffer()
        const val MAX_EVENTS = 1024
    }
}