package com.severett.androidxdemo.ui.serialization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SerializationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the serialization Fragment"
    }
    val text: LiveData<String> = _text
}