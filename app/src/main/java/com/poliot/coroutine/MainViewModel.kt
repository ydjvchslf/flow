package com.poliot.coroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poliot.coroutine.util.DebugLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    init {
        DebugLog.i(logTag, "init-()")
        _currentValue.value = 0 // 초기값 설정
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
}