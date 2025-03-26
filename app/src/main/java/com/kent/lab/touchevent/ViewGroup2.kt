package com.kent.lab.touchevent

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import com.kent.lab.touchevent.ViewGroup2
import java.lang.Math.abs

/**
 * Created by apple on 2017/6/3.
 */
class ViewGroup2 : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var downX = 0f
    private var downY = 0f
    private var isClicking = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "dispatchTouchEvent    action:" + StringUtils.getMotionEventName(ev))
        val superReturn = super.dispatchTouchEvent(ev)
        Log.d(TAG, "dispatchTouchEvent    action:" + StringUtils.getMotionEventName(ev) + " " + superReturn)
        return superReturn
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(event))

//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                downX = event.x
//                downY = event.y
//                isClicking = true
//                Log.d(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true)
//                return true // 攔截 DOWN，防止子 View 消費
//            }

//            MotionEvent.ACTION_MOVE -> {
//                val dx = event.x - downX
//                val dy = event.y - downY
//                Log.d(TAG, "dx=$dx, dy=$dy, touchSlop=$touchSlop")
//
//                if (abs(dx) > touchSlop || abs(dy) > touchSlop) {
//                    isClicking = false
//                    Log.d(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + false+"(abs > touchSlop)")
//                    return false // 不攔截 MOVE，讓滑動傳遞
//                }
//                Log.d(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true);
//                return true
//            }

//            MotionEvent.ACTION_UP -> {
//                if (isClicking) {
//                    Log.d(TAG, "ViewGroup2 被點擊了！");
//                    println("ViewGroup 被點擊了！")
//                }
//                isClicking = false
//                Log.d(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true);
//                return true
//            }

//            MotionEvent.ACTION_CANCEL -> {
//                isClicking = false
//                Log.d(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true);
//                return true
//            }
//        }
        return super.onInterceptTouchEvent(event)
//        Log.i(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(ev))
//        val superReturn = super.onInterceptTouchEvent(ev)
//        Log.d(TAG, "onInterceptTouchEvent action:" + StringUtils.getMotionEventName(ev) + " " + superReturn)
//        return superReturn
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent          action:" + StringUtils.getMotionEventName(event))

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                isClicking = true
                Log.d(TAG, "onTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true)
                return true // 攔截 DOWN，防止子 View 消費
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downX
                val dy = event.y - downY
                Log.d(TAG, "dx=$dx, dy=$dy, touchSlop=$touchSlop")

                if (abs(dx) > touchSlop || abs(dy) > touchSlop) {
                    isClicking = false
                    Log.d(TAG, "onTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + false+"(abs > touchSlop)")
                    return false // 不攔截 MOVE，讓滑動傳遞
                }
                Log.d(TAG, "onTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true);
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (isClicking) {
                    Log.d(TAG, "ViewGroup2 被點擊了！");
                    println("ViewGroup 被點擊了！")
                }
                isClicking = false
                Log.d(TAG, "onTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true);
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                isClicking = false
                Log.d(TAG, "onTouchEvent action:" + StringUtils.getMotionEventName(event) + " " + true);
                return true
            }
        }
        return super.onTouchEvent(event)

//        if (ev.action == MotionEvent.ACTION_DOWN || ev.action == MotionEvent.ACTION_UP) {
//            Log.d(TAG, "onTouchEvent          action:" + StringUtils.getMotionEventName(ev) + " " + true)
//            return true
//        } else {
//            Log.d(TAG, "onTouchEvent          action:" + StringUtils.getMotionEventName(ev) + " " + false)
//            return false
//        }
    }

    companion object {
        private val TAG: String = ViewGroup2::class.java.name
    }
}