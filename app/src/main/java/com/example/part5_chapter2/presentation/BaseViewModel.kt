package com.example.part5_chapter2.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

internal abstract class BaseViewModel: ViewModel() {

    abstract fun fetchData(): Job

}