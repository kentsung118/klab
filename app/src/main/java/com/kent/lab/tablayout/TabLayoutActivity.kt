package com.kent.lab.tablayout

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.kent.lab.BaseBindingActivity
import com.kent.lab.R
import com.kent.lab.databinding.ActivityTabLayoutBinding

class TabLayoutActivity : BaseBindingActivity<ActivityTabLayoutBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTabLayoutBinding
        get() = ActivityTabLayoutBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.pager.adapter = SimpleFragmentPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout1, binding.pager) { tab, position ->
            if (position == 2) {
                val customView = LayoutInflater.from(this).inflate(R.layout.item_tab, null)
                val tabText = customView.findViewById<TextView>(R.id.tab_title)
                tabText.text = "Tab${position + 1}"
                tabText.setTextColor(Color.RED)
                tab.customView = customView
            } else {
                tab.text = "Tab${position + 1}"
            }
        }.attach()

        binding.tabLayout1.addOnTabSelectedListener(object:OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    // 選中狀態：所有 Tab 統一設為藍色
                    if (it.customView != null) {
                        val tabText = it.customView!!.findViewById<TextView>(R.id.tab_title)
                        tabText.setTextColor(Color.BLUE)
                    } else {

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    // 未選中狀態：索引為 2 的 Tab 設為紅色，其他設為灰色
                    if (it.position == 2 && it.customView != null) {
                        val tabText = it.customView!!.findViewById<TextView>(R.id.tab_title)
                        tabText.setTextColor(Color.RED)
                    } else {
//                        it.view.setTextColor(Color.GRAY)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    class SimpleFragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        private val tabTitles by lazy {
            val list = ArrayList<String>()
            for (index in 1..10) {
                list.add("tab$index")
            }
            list
        }
        private val fragment by lazy {
            val list = ArrayList<Fragment>()
            for (index in 1..10) {
                list.add(TextFragment("Fragment$index"))
            }
            list
        }

        override fun getItemCount(): Int {
            return fragment.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragment[position]
        }
    }
}