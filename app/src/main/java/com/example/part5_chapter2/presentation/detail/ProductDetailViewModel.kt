package com.example.part5_chapter2.presentation.detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.domain.GetProductItemUseCase
import com.example.part5_chapter2.domain.OrderProductItemUseCase
import com.example.part5_chapter2.presentation.BaseViewModel
import com.example.part5_chapter2.presentation.list.ProductListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProductDetailViewModel(
    private val productId: Long,
    private val getProductItemUseCase: GetProductItemUseCase,
    private val orderProductItemUseCase: OrderProductItemUseCase
) : BaseViewModel() {

    private var _productDetailStateLiveData =
        MutableLiveData<ProductDetailState>(ProductDetailState.UnInitialized)
    val productDetailStateLiveData: LiveData<ProductDetailState> = _productDetailStateLiveData

    private lateinit var productEntity: ProductEntity

    override fun fetchData(): Job =viewModelScope.launch {
        Log.d("아이디2", productId.toString())
        Log.d("결과",getProductItemUseCase(productId).toString())
        setState(ProductDetailState.Loading)
        Log.d("로딩","로딩")
        getProductItemUseCase(productId)?.let { product ->
            Log.d("제품",product.toString())
            productEntity = product
            setState(ProductDetailState.Success(productEntity))
        } ?: kotlin.run {
            if (::productEntity.isInitialized){
                Log.d("에러1",productEntity.toString())
            }

        }
    }

    private fun setState(state:ProductDetailState){
        _productDetailStateLiveData.postValue(state)

    }

    fun orderProduct() = viewModelScope.launch{
        if (::productEntity.isInitialized){
            val productId =orderProductItemUseCase(productEntity)
            if (productEntity.id==productId){
                setState(ProductDetailState.Order)
            } else{
                setState(ProductDetailState.Error)
            }
        }

    }
}