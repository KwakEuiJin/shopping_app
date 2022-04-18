package com.example.part5_chapter2.domain

import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.data.repository.ProductRepository

internal class OrderProductItemUseCase(private val productRepository: ProductRepository) : UseCase {
    suspend operator fun invoke(productEntity: ProductEntity): Long {
        return productRepository.insertProductItem(productEntity)
    }
}