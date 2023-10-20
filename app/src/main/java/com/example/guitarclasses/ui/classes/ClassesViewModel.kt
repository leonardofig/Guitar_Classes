package com.example.guitarclasses.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClassesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Classes Fragment"
    }
    val text: LiveData<String> = _text
}