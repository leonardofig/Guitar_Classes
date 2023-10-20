package com.example.guitarclasses.ui.students

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is students Fragment"
    }
    val text: LiveData<String> = _text
}