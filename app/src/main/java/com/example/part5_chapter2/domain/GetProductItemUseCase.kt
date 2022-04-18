package com.example.part5_chapter2.domain

import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.data.repository.ProductRepository

internal class GetProductItemUseCase(private val productRepository: ProductRepository) : UseCase {
    suspend operator fun invoke(productId: Long): ProductEntity? {
        return productRepository.getProductItem(productId)
    }
}