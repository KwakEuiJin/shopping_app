package com.example.part5_chapter2.domain

import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.data.repository.ProductRepository

internal class GetProductListUseCase(private val productRepository: ProductRepository) : UseCase {
   suspend operator fun invoke():List<ProductEntity>{
       return productRepository.getProductList()
   }
}
