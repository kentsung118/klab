package com.kent.lab.touchevent

import android.view.MotionEvent

object StringUtils {
    @JvmStatic
    fun getMotionEventName(ev: MotionEvent): String {
        val result = when (ev.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            MotionEvent.ACTION_MASK -> "ACTION_MASK"
            else -> "" + ev.action
        }
        return result
    }
}
