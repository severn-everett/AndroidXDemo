package com.severett.androidxdemo.ui.serialization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SerializationViewModel : ViewModel() {
    val serializedFooText = MutableLiveData<String>()
    val deserializedFooText = MutableLiveData<String>()
}