package com.example.part5_chapter2.presentation.list

import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.domain.GetProductListUseCase

sealed class ProductListState{
    object UnInitialized: ProductListState()
    object Loading: ProductListState()

    data class Success(
        val productList: List<ProductEntity>
    ):ProductListState()

    object Error: ProductListState()

}
