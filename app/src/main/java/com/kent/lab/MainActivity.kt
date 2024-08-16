package com.kent.lab

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kent.lab.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseBindingActivity<ActivityMainBinding>(){

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate


    private val mTitle = arrayOf(
        "Download",
//        "Download Video",
//        "WebsocketActivity",
//        "AudioActivity",
//        "Transition Drawable",
//        "Constraint Layout",
//        "DesktopManager",
//        "WorkManager",
//        "Room",
//        "OkHttpClientActivity",
//        "Third party Share",
//        "Animation Drawable",
//        "RetrofitActivity",
//        "CountdownTimerActivity"
    )
    private val mClasses = arrayOf<Class<*>>(
        DownloadActivity::class.java,
//        EditTextActivity::class.java,
//        WebsocketActivity::class.java,
//        AudioActivity::class.java,
//        TransitionDrawableActivity::class.java,
//        ConstransLayoutDemoActivity::class.java,
//        DesktopManagerActivity::class.java,
//        WorkActivity::class.java,
//        RoomActivity::class.java,
//        OkHttpClientActivity::class.java,
//        ShareActivity::class.java,
//        AnimationActivityKt::class.java,
//        RetrofitActivity::class.java,
//        CountdownTimerActivity::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.list.setAdapter(ArrayAdapter(this, R.layout.item_list, R.id.tv_items, mTitle))
        initListener()
    }

    private fun initListener() {
        binding.list.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
            startActivity(
                Intent(this@MainActivity, mClasses[i])
            )
        }
    }


}