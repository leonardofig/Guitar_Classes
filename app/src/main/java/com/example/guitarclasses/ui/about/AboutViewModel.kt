package com.example.guitarclasses.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {


    private val _textExtra1 = MutableLiveData<String>().apply {
        value = "DBhelper, Toast, Spinner"
    }
    val textExtra1: LiveData<String> = _textExtra1

    private val _textExtra2 = MutableLiveData<String>().apply {
        value = "Fragments"
    }
    val textExtra2: LiveData<String> = _textExtra2

    private val _textExtra3 = MutableLiveData<String>().apply {
        value = "You can only create up to 3 users."
    }
    val textExtra3: LiveData<String> = _textExtra3

    private val _textExtra4 = MutableLiveData<String>().apply {
        value = "ListView - Ascending or descending order  "
    }
    val textExtra4: LiveData<String> = _textExtra4

    private val _textExtra5 = MutableLiveData<String>().apply {
        value = "Insert, edit and delete students and classes"
    }
    val textExtra5: LiveData<String> = _textExtra5

    private val _textExtra6 = MutableLiveData<String>().apply {
        value = ""
    }
    val textExtra6: LiveData<String> = _textExtra6
}
