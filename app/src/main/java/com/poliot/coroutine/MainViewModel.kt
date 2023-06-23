package com.poliot.coroutine

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poliot.coroutine.util.DebugLog
import com.poliot.coroutine.util.textChangesToFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

enum class NumberActionType {
    PLUS,
    MINUS
}

class MainViewModel: ViewModel() {

    private val logTag = MainViewModel::class.simpleName

    // 값이 변경될 것이기 때문에 var
    var value = 0

    // 내부에서 설정하는 자료형은 뮤터블 (변경 가능)
    private val _currentValue = MutableStateFlow<Int>(0) // 초기값
    // 변경되지 않는 데이터를 가져올 때 이름을 _ 언더스코어 없이 설정
    val currentValue: StateFlow<Int>
        get() = _currentValue

    private val _currentUserInput = MutableStateFlow<String>("")
    val currentUserInput: StateFlow<String> = _currentUserInput

    private val _isWorking = MutableSharedFlow<Boolean>()
    val isWorking: SharedFlow<Boolean> = _isWorking

    val currentValueInfo: Flow<String> = _currentValue.map { "현재값: $it" }

    init {
        DebugLog.i(logTag, "init-()")
        _currentValue.value = 0 // 초기값 설정
    }

    fun bindUserInputEditText(editText: EditText) {
        viewModelScope.launch {
            editText.textChangesToFlow().collect() {
                _currentUserInput.value = it.toString()
            }
        }
    }

    // 뷰모델이 가지고 있는 값을 변경하는 메소드
    fun updateValue(actionType: NumberActionType, input: Int) {
        viewModelScope.launch {
            _isWorking.emit(true)
            when (actionType) {
                NumberActionType.PLUS -> {
                    delay(2000)
                    _currentValue.emit(_currentValue.value.plus(input))
                    _isWorking.emit(false)
                }
                NumberActionType.MINUS -> {
                    delay(2000)
                    _currentValue.emit(_currentValue.value.minus(input))
                    _isWorking.emit(false)
                }
            }
        }
        this._currentUserInput.value = ""
    }

    fun backgroundWork(resultStr: (String) -> Unit) {
        DebugLog.i(logTag, "backgroundWork-()")
        viewModelScope.launch(Dispatchers.IO) {
            _isWorking.emit(true)
            val time = measureTimeMillis {
                val answer1 = async { networkCall() }
                val answer2 = async { networkCall2(answer1) }
                DebugLog.d(logTag, "Answer1 is ${answer1.await()}")
                DebugLog.d(logTag, "Answer2 is ${answer2.await()}")
                resultStr.invoke(answer2.await())
                _isWorking.emit(false)
            }
            DebugLog.d(logTag, "소요시간: $time ms.")
        }
    }

    suspend fun networkCall(): String {
        DebugLog.i(logTag, "networkCall-()")
        delay(3000)
        return "My name is"
    }

    suspend fun networkCall2(input: Deferred<String>): String {
        DebugLog.i(logTag, "networkCall2-()")
        return "${input.await()} Mia"
    }
}