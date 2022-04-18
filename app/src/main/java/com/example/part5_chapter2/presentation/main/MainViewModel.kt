package com.example.part5_chapter2.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.part5_chapter2.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


internal class MainViewModel : BaseViewModel() {

    private val _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData:LiveData<MainState> = _mainStateLiveData

    override fun fetchData(): Job = Job()

    fun refreshOrderList() = viewModelScope.launch {
        _mainStateLiveData.postValue(MainState.RefreshOrderList)
    }
}