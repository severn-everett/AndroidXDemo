package com.severett.androidxdemo.ui.atomicfu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val DEFAULT_TEXT = "This is the AtomicFU Fragment"

class AtomicFUViewModel : ViewModel() {
    val text = MutableLiveData<String>()

    fun resetText() {
        text.value = DEFAULT_TEXT
    }
}