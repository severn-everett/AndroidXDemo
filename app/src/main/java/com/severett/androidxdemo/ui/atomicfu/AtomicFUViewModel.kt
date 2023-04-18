package com.severett.androidxdemo.ui.atomicfu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AtomicFUViewModel : ViewModel() {
    val safeResultVal = MutableLiveData<String>()
    val unsafeResultVal = MutableLiveData<String>()
}