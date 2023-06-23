package com.poliot.coroutine.third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.poliot.coroutine.R
import com.poliot.coroutine.databinding.FragmentThirdBinding
import com.poliot.coroutine.util.DebugLog

class ThirdFragment: Fragment() {

    private val logTag = ThirdFragment::class.simpleName
    private lateinit var binding: FragmentThirdBinding
    private val thirdViewModel: ThirdViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DebugLog.i(logTag, "onCreateView-()")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_third, container, false)
        with(binding) {
            viewModel = thirdViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}