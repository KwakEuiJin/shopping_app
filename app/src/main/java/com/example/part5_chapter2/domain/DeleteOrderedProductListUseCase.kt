package com.example.part5_chapter2.domain

import com.example.part5_chapter2.data.repository.ProductRepository

internal class DeleteOrderedProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke() {
        return productRepository.deleteAll()
    }

}