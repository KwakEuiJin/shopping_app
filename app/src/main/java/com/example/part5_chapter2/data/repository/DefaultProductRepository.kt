package com.example.part5_chapter2.data.repository

import android.util.Log
import com.example.part5_chapter2.data.db.dao.ProductDao
import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.data.network.ProductApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class DefaultProductRepository(
    private val productApiService: ProductApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val productDao: ProductDao

) : ProductRepository {
    override suspend fun getProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        val response = productApiService.getProducts()
        return@withContext (if (response.isSuccessful) {
            response.body()?.items?.map {
                it.toEntity()
            } ?: listOf()
        } else{
            Log.d("상품 에러",response.body().toString())
            listOf()
        })
    }

    override suspend fun getLocalProductList(): List<ProductEntity> = withContext(ioDispatcher) {
        productDao.getAll()
    }

    override suspend fun insertProductItem(productItem: ProductEntity): Long =
        withContext(ioDispatcher) {
            productDao.insert(productItem)
        }

    override suspend fun insertProductList(productList: List<ProductEntity>) =
        withContext(ioDispatcher) {
            TODO("Not yet implemented")
        }

    override suspend fun updateProductItem(productItem: ProductEntity) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductItem(itemId: Long): ProductEntity? = withContext(ioDispatcher) {
        val response = productApiService.getProduct(itemId)
        Log.d("레포지토리",response.body().toString())
        return@withContext (if (response.isSuccessful) {
            response.body()?.toEntity()
        } else{
            null
        })
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        productDao.deleteAll()
    }

    override suspend fun deleteProductItem(id: Long) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}