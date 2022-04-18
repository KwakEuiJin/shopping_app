package com.example.part5_chapter2.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.part5_chapter2.domain.GetProductListUseCase
import com.example.part5_chapter2.presentation.BaseViewModel
import com.example.part5_chapter2.presentation.profile.ProfileState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductListViewModel(private val getProductListUseCase: GetProductListUseCase) :
    BaseViewModel() {

    private var _productListStateLiveData =
        MutableLiveData<ProductListState>(ProductListState.UnInitialized)
    val productListStateLiveData: LiveData<ProductListState> = _productListStateLiveData


    override fun fetchData(): Job = viewModelScope.launch {
        _productListStateLiveData.postValue(ProductListState.Loading)
        _productListStateLiveData.postValue(ProductListState.Success(getProductListUseCase()))
    }

}