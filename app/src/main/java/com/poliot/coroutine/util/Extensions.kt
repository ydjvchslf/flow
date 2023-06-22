package com.poliot.coroutine.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangesToFlow(): Flow<CharSequence?> {

    val logTag = "Extensions"

    // flow 콜백 받기
    return callbackFlow<CharSequence?> {

        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Unit
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                DebugLog.i(logTag, "onTextChanged-()")
                DebugLog.d(logTag, "textChangesToFlow() 에 달려있는 텍스트 와쳐 / text: $text")
                // 값 내보내기
                trySend(text).isSuccess
            }

            override fun afterTextChanged(p0: Editable?) {
                Unit
            }
        }
        addTextChangedListener(listener)

        awaitClose {
            DebugLog.d(logTag, "textChangesToFlow() awaitClose 실행")
            removeTextChangedListener(listener)
        }
    }
}
