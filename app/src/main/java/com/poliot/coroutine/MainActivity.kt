package com.poliot.coroutine

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.*
import com.poliot.coroutine.databinding.ActivityMainBinding
import com.poliot.coroutine.util.DebugLog
import com.poliot.coroutine.util.textChangesToFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    private val logTag = MainActivity::class.simpleName
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의
        // 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시 합니다.
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.numberInputEdittext.textChangesToFlow()

        val job = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.currentValueInfo.collect {
                        DebugLog.d(logTag, "mainViewModel.currentValue: $it")
                        binding.numberTextview.text = it
                    }
                }

                launch {
                    mainViewModel.currentUserInput.collect {
                        DebugLog.d(logTag, "mainViewModel.currentUserInput: $it")
                        binding.numberInputEdittext.setText(it)
                    }
                }

                launch {
                    mainViewModel.isWorking.collect { isWorking ->
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

        mainViewModel.bindUserInputEditText(binding.numberInputEdittext)

        // 리스너 연결
        binding.plusBtn.setOnClickListener(this)
        binding.minusBtn.setOnClickListener(this)
        binding.workBtn.setOnClickListener(this)


    }

    // 액티비티가 파괴될 때
    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
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
                mainViewModel.backgroundWork {
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
