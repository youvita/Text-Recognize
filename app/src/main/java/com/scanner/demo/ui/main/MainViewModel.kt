package com.scanner.demo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _camera: MutableLiveData<Boolean> = MutableLiveData()
    val camera: LiveData<Boolean> get() = _camera

    /**
     * binding function to open camera
     * */
    fun openCamera() {
        _camera.postValue(true)
    }
}