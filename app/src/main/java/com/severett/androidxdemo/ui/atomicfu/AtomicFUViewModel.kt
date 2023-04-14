package com.severett.androidxdemo.ui.atomicfu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AtomicFUViewModel : ViewModel() {

    val text = MutableLiveData<String>().apply {
        value = "This is the AtomicFU Fragment"
    }
}