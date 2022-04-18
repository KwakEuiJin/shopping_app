package com.example.part5_chapter2.data.network

import com.example.part5_chapter2.data.response.ProductResponse
import com.example.part5_chapter2.data.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("products")
    suspend fun getProducts():Response<ProductsResponse>

    @GET("products/{productId}")
    suspend fun getProduct(@Path("productId")productId:Long):Response<ProductResponse>

}