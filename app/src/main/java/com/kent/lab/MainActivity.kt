package com.kent.lab

import android.content.Intent
import android.os.Build.BRAND
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.kent.lab.databinding.ActivityMainBinding
import com.kent.lab.tablayout.TabLayoutActivity
import com.kent.lab.touchevent.TouchEventActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate


    private val mTitle = arrayOf(
        "DownloadManager",
        "WorkManager",
        "MaskView",
        "MediaStoreActivity",
        "TouchEventActivity",
        "TabLayout Activity",
        "GlideActivityt",
        "Null Safe Test",
//        "WorkManager",
//        "Room",
//        "OkHttpClientActivity",
//        "Third party Share",
//        "Animation Drawable",
//        "RetrofitActivity",
//        "CountdownTimerActivity"
    )
    private val mClasses = arrayOf<Class<*>>(
        DownloadManagerActivity::class.java,
        WorkManagerActivity::class.java,
        MaskViewActivity::class.java,
        MediaStoreActivity::class.java,
        TouchEventActivity::class.java,
        TabLayoutActivity::class.java,
        GlideActivity::class.java,
        NullSafeActivity::class.java,
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

        Log.d("lala", BRAND)
    }

    private fun initListener() {
        binding.list.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
            startActivity(
                Intent(this@MainActivity, mClasses[i])
            )
        }
    }


}