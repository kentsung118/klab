package com.kent.lab.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kent.lab.BaseBindingActivity
import com.kent.lab.databinding.ActivityTabLayoutBinding

class TabLayoutActivity : BaseBindingActivity<ActivityTabLayoutBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTabLayoutBinding
        get() = ActivityTabLayoutBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        for (index in 1..10) {
////            val tab = binding.tabLayout1.newTab()
////            tab.text = "tab$index"
////            binding.tabLayout1.addTab(tab)
////        }

        binding.pager.adapter = SimpleFragmentPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout1, binding.pager) { tab, position ->
            tab.text = "tab$position"
        }.attach()

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