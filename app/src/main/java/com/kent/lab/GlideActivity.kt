package com.kent.lab

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.kent.lab.databinding.ActivityGlideBinding


class GlideActivity : BaseBindingActivity<ActivityGlideBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityGlideBinding
        get() = ActivityGlideBinding::inflate

    val imageUrl = "http://cdn.17app.co/b053a3ce-45a6-4446-a9eb-18c118195b06.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btn1.setOnClickListener {
            Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .skipMemoryCache(true)
                .into(binding.image)


        }

        binding.btn2.setOnClickListener {
            Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                .skipMemoryCache(true)
                .into(binding.image)
        }

        binding.btn3.setOnClickListener {
            Glide.with(this).clear(binding.image)
//            Glide.with(this).clearOnStop()
        }




    }

}