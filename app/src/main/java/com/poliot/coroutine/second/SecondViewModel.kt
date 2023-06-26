package com.poliot.coroutine.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poliot.coroutine.retrofit.RemoteDataSource
import com.poliot.coroutine.util.DebugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondViewModel: ViewModel() {

    private val logTag = SecondViewModel::class.simpleName
    private val repository = RemoteDataSource()

    init {
        DebugLog.i(logTag, "init-()")
    }

    fun getAllProduct() {
        DebugLog.i(logTag, "getAllProduct-()")
        viewModelScope.launch(Dispatchers.IO) {
            val productList = repository.getAllProducts()
            DebugLog.d(logTag, "size: ${productList?.size}")
        }
    }
}