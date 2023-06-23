package com.poliot.coroutine.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.poliot.coroutine.R
import com.poliot.coroutine.databinding.FragmentSecondBinding
import com.poliot.coroutine.util.DebugLog

class SecondFragment: Fragment() {

    private val logTag = SecondFragment::class.simpleName
    private lateinit var binding: FragmentSecondBinding
    private val secondViewModel: SecondViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DebugLog.i(logTag, "onCreateView-()")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
        with(binding) {
            viewModel = secondViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}