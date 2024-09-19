package com.kent.lab.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.kent.lab.BaseBindingDialogFragment
import com.kent.lab.R
import com.kent.lab.databinding.LayoutDownloadMaskBinding

class MaskDialog(val lockScreen: Boolean = false) : BaseBindingDialogFragment<LayoutDownloadMaskBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> LayoutDownloadMaskBinding
        get() = LayoutDownloadMaskBinding::inflate

    override fun onStart() {
        super.onStart()

        if (lockScreen) {
            isCancelable = false
            dialog?.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                setDimAmount(0F)
                val params: WindowManager.LayoutParams = attributes
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.MATCH_PARENT
                attributes = params
            }
            binding?.mask?.isVisible = true
            return
        } else {
          binding?.mask?.isVisible = false
        }

        // 获取对话框的 Window，并设置 Window 的属性
        dialog?.window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

            // 设置背景透明
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0F)

            // 设置不拦截触摸事件，允许事件传递给下层视图
            setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,  // 不拦截触摸事件
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )

            // 设置对话框不获取焦点
            setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // 不获取焦点
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )

            // 可选：调整对话框大小
            val params: WindowManager.LayoutParams = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params

            setGravity(Gravity.BOTTOM)

        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lockScreen) {
            binding?.mask?.visibility = View.VISIBLE
        }
        binding?.cancel?.setOnClickListener {
            dismiss()
        }
    }
}