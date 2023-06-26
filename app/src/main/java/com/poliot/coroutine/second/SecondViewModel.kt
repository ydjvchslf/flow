package com.poliot.coroutine.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poliot.coroutine.retrofit.RemoteDataSource
import com.poliot.coroutine.util.DebugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SecondViewModel: ViewModel() {

    private val logTag = SecondViewModel::class.simpleName
    private val repository = RemoteDataSource()

    private val _isWorking = MutableStateFlow<Boolean>(false)
    val isWorking: StateFlow<Boolean> = _isWorking

    init {
        DebugLog.i(logTag, "init-()")
    }

    fun getAllProduct() {
        DebugLog.i(logTag, "getAllProduct-()")
        viewModelScope.launch(Dispatchers.IO) {
            _isWorking.emit(true)
            val productList = repository.getAllProducts()
            DebugLog.d(logTag, "size: ${productList?.size}")
            _isWorking.emit(false)
        }
    }

    fun getOneProduct(id: String) {
        DebugLog.i(logTag, "getOneProduct-()")
        viewModelScope.launch(Dispatchers.IO) {
            _isWorking.emit(true)
            repository.getOneProduct(id)
            _isWorking.emit(false)
        }
    }
}