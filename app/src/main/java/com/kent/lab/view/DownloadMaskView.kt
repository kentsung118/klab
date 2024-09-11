package com.kent.lab.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.kent.lab.databinding.LayoutDownloadMaskBinding

@SuppressLint("ClickableViewAccessibility")
class DownloadMaskView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding: LayoutDownloadMaskBinding = LayoutDownloadMaskBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.mask.setOnTouchListener { _, _ ->
            true
        }
        binding.cancel.setOnClickListener {
            hideProgress()
        }
    }

    fun showProgress(lockScreen: Boolean = false) {
        binding.mask.isVisible = lockScreen
        binding.rootLayout.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding.mask.visibility = View.GONE
        binding.rootLayout.visibility = View.GONE
    }

}