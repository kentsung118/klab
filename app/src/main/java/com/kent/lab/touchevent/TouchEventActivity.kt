package com.kent.lab.touchevent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import com.kent.lab.BaseBindingActivity
import com.kent.lab.databinding.ActivityTouchEventBinding


class TouchEventActivity: BaseBindingActivity<ActivityTouchEventBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTouchEventBinding
        get() = ActivityTouchEventBinding::inflate

    private val TAG: String = TouchEventActivity::class.java.name;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?: return super.dispatchTouchEvent(ev)
        Log.i(TAG, "dispatchTouchEvent    action:" + StringUtils.getMotionEventName(ev))
        val superReturn = super.dispatchTouchEvent(ev)
        Log.d(TAG, "dispatchTouchEvent    action:" + StringUtils.getMotionEventName(ev) + " " + superReturn)
        return superReturn
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev?: return super.dispatchTouchEvent(ev)
        Log.i(TAG, "onTouchEvent          action:" + StringUtils.getMotionEventName(ev))
        val superReturn = super.onTouchEvent(ev)
        Log.d(TAG, "onTouchEvent          action:" + StringUtils.getMotionEventName(ev) + " " + superReturn)
        return superReturn
    }
}