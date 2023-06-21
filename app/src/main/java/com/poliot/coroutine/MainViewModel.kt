package com.poliot.coroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.poliot.coroutine.util.DebugLog

enum class NumberActionType {
    PLUS,
    MINUS
}

class MainViewModel: ViewModel() {

    private val logTag = MainViewModel::class.simpleName

    // 값이 변경될 것이기 때문에 var
    var value = 0

    // 내부에서 설정하는 자료형은 뮤터블 (변경 가능)
    private val _currentValue = MutableLiveData<Int>()
    // 변경되지 않는 데이터를 가져올 때 이름을 _ 언더스코어 없이 설정
    val currentValue: LiveData<Int>
        get() = _currentValue

    private val _currentUserInput = MutableLiveData<String>()
    val currentUserInput: LiveData<String> = _currentUserInput

    init {
        DebugLog.i(logTag, "init-()")
        _currentValue.value = 0 // 초기값 설정
    }

    // 뷰모델이 가지고 있는 값을 변경하는 메소드
    fun updateValue(actionType: NumberActionType, input: Int) {
        when (actionType) {
            NumberActionType.PLUS -> {
                _currentValue.value = _currentValue.value?.plus(input)
            }
            NumberActionType.MINUS -> {
                _currentValue.value = _currentValue.value?.minus(input)
            }
        }
        this._currentUserInput.value = ""
    }

}