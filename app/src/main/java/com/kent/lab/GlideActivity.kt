package com.kent.lab

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kent.lab.databinding.ActivityGlideBinding


class GlideActivity : BaseBindingActivity<ActivityGlideBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityGlideBinding
        get() = ActivityGlideBinding::inflate

    val imageUrl = "http://cdn.17app.co/b053a3ce-45a6-4446-a9eb-18c118195b06.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btn1.setOnClickListener {
            Glide.with(this)
                .asBitmap()

                .apply(RequestOptions()
//                    .disallowHardwareConfig()
                    .format(DecodeFormat.PREFER_ARGB_8888))
                .load(imageUrl)
//                .into()
//                .skipMemoryCache(true)
                .into(binding.image)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                        Log.d("Glide", "btn1 Bitmap Config = ${resource.config}")
//                        binding.image.setImageBitmap(resource)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {}
//                })

        }


        binding.btn2.setOnClickListener {
            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions()
//                    .disallowHardwareConfig()
                    .format(DecodeFormat.PREFER_RGB_565))
//                .skipMemoryCache(true)
                .into(binding.image)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                        Log.d("Glide", "btn2 Bitmap Config = ${resource.config}")
//                        binding.image.setImageBitmap(resource)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {}
//                })

        }

        binding.btn3.setOnClickListener {
            Glide.with(this).clear(binding.image)
            binding.image.setImageDrawable(null)
//            Glide.with(this).clearOnStop()
        }




    }

}