package com.example.part5_chapter2.data.response

import com.example.part5_chapter2.data.entity.product.ProductEntity
import java.util.*

data class ProductResponse(
    val id: Long,
    val createdAt: Long,
    val productName: String,
    val productPrice: Int,
    val productImage: String,
    val productType: String,
    val productIntroductionImage: String
) {
    fun toEntity(): ProductEntity =
        ProductEntity(
            id,
            Date(createdAt),
            productName,
            productPrice,
            productImage,
            productType,
            productIntroductionImage
        )
}
