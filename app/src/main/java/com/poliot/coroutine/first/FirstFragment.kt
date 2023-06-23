package com.poliot.coroutine.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.poliot.coroutine.R
import com.poliot.coroutine.databinding.FragmentFirstBinding
import com.poliot.coroutine.util.DebugLog
import com.poliot.coroutine.util.textChangesToFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirstFragment: Fragment(), View.OnClickListener {

    private val logTag = FirstFragment::class.simpleName
    private lateinit var binding: FragmentFirstBinding
    private val firstViewModel: FirstViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DebugLog.i(logTag, "onCreateView-()")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        with(binding) {
            viewModel = firstViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DebugLog.i(logTag, "onViewCreated-()")

        binding.numberInputEdittext.textChangesToFlow()

        val job = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    firstViewModel.currentValueInfo.collect {
                        DebugLog.d(logTag, "mainViewModel.currentValue: $it")
                        binding.numberTextview.text = it
                    }
                }

                launch {
                    firstViewModel.currentUserInput.collect {
                        DebugLog.d(logTag, "mainViewModel.currentUserInput: $it")
                        binding.numberInputEdittext.setText(it)
                    }
                }

                launch {
                    firstViewModel.isWorking.collect { isWorking ->
                        when (isWorking) {
                            true -> {
                                binding.loadingBar.visibility = View.VISIBLE
                            }
                            false -> {
                                binding.loadingBar.visibility = View.INVISIBLE
                            }
                        }
                    }
                }

                launch {
                    binding.numberInputEdittext.textChangesToFlow()
                        .collect() { input ->
                            DebugLog.d(logTag, "textChangesToFlow: $input")
                        }
                }
            }
        }

        /*
        mainViewModel.currentValue.observe(this, Observer {
            DebugLog.d(logTag, "mainViewModel.currentValue 라이브 데이터 값 변경: $it")
            binding.numberTextview.text = it.toString()
        })

        mainViewModel.currentUserInput.observe(this, Observer { changedUserInput ->
            DebugLog.d(logTag, "mainViewModel.currentUserInput 라이브 데이터 값 변경: $changedUserInput")
            binding.numberInputEdittext.setText(changedUserInput)
        })
         */

        firstViewModel.bindUserInputEditText(binding.numberInputEdittext)

        // 리스너 연결
        binding.plusBtn.setOnClickListener(this)
        binding.minusBtn.setOnClickListener(this)
        binding.workBtn.setOnClickListener(this)


    }

    // 액티비티가 파괴될 때
    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        super.onDestroy()
    }

    override fun onClick(view: View?) {

//        val userInputString = binding.numberInputEdittext.text.toString()
//
//        if (userInputString.isEmpty()) {
//            return
//        }
//
//        val userInputNumber = userInputString.toInt()

        // 뷰모델에 라이브데이터 값을 변경하는 메소드 실행
        when (view) {
            binding.plusBtn -> {
                //mainViewModel.updateValue(NumberActionType.PLUS, userInputNumber)
            }
            binding.minusBtn -> {
                //mainViewModel.updateValue(NumberActionType.MINUS, userInputNumber)
            }
            binding.workBtn -> {
                firstViewModel.backgroundWork {
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}