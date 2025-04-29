package com.kent.lab.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kent.lab.BaseBindingFragment
import com.kent.lab.databinding.FragmentTextBinding

class TextFragment(val title: String) : BaseBindingFragment<FragmentTextBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTextBinding
        get() = FragmentTextBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.title?.text = title
    }
}