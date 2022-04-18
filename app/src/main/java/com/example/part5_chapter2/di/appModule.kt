package com.example.part5_chapter2.di

import com.example.part5_chapter2.data.db.dao.provideDB
import com.example.part5_chapter2.data.db.dao.provideTodoDao
import com.example.part5_chapter2.data.network.buildOkhttpClient
import com.example.part5_chapter2.data.network.provideGsonConverterFactory
import com.example.part5_chapter2.data.network.provideProductApiService
import com.example.part5_chapter2.data.network.provideProductRetrofit
import com.example.part5_chapter2.data.preference.PreferenceManager
import com.example.part5_chapter2.data.repository.DefaultProductRepository
import com.example.part5_chapter2.data.repository.ProductRepository
import com.example.part5_chapter2.domain.*
import com.example.part5_chapter2.domain.DeleteOrderedProductListUseCase
import com.example.part5_chapter2.domain.GetOrderedProductListUseCase
import com.example.part5_chapter2.domain.GetProductItemUseCase
import com.example.part5_chapter2.domain.GetProductListUseCase
import com.example.part5_chapter2.domain.OrderProductItemUseCase
import com.example.part5_chapter2.presentation.detail.ProductDetailViewModel
import com.example.part5_chapter2.presentation.list.ProductListViewModel
import com.example.part5_chapter2.presentation.main.MainViewModel
import com.example.part5_chapter2.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    //coroutine
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    //useCase
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }
    factory { GetOrderedProductListUseCase(get()) }
    factory { DeleteOrderedProductListUseCase(get()) }

    //repository
    single<ProductRepository> { DefaultProductRepository(get(), get(),get()) }

    //viewModel
    viewModel{MainViewModel()}
    viewModel{ProductListViewModel(get())}
    viewModel{ProfileViewModel(get(),get(),get())}
    viewModel {(productId:Long) -> ProductDetailViewModel(productId,get(),get()) }


    //Database
    single { provideDB(androidContext()) }
    single { provideTodoDao(get()) }
    single { PreferenceManager(androidContext()) }


    single { provideGsonConverterFactory() }
    single { buildOkhttpClient() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }

}