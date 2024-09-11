package com.kent.lab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.kent.lab.databinding.ActivityMaskViewBinding

class MaskViewActivity : BaseBindingActivity<ActivityMaskViewBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMaskViewBinding
        get() = ActivityMaskViewBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btn1.setOnClickListener {
            Log.d("lala", "btn1 click")
            binding.downloadMask.visibility = View.VISIBLE
            binding.downloadMask.showProgress()
        }

        binding.text.setOnClickListener {
            Log.d("lala", "text click")
        }


    }

}