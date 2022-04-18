package com.example.part5_chapter2.presentation.detail

import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.domain.GetProductListUseCase

sealed class ProductDetailState {
    object UnInitialized : ProductDetailState()
    object Loading : ProductDetailState()

    data class Success(
        val productEntity: ProductEntity
    ) : ProductDetailState()

    object Order : ProductDetailState()

    object Error : ProductDetailState()

}
