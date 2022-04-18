package com.example.part5_chapter2.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.example.part5_chapter2.R
import com.example.part5_chapter2.databinding.ActivityProductDetailBinding
import com.example.part5_chapter2.extension.load
import com.example.part5_chapter2.extension.loadCenterCrop
import com.example.part5_chapter2.extension.toast
import com.example.part5_chapter2.presentation.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

internal class ProductDetailActivity :
    BaseActivity<ProductDetailViewModel, ActivityProductDetailBinding>() {

    companion object {
        const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"
        const val PRODUCT_ORDER_RESULT_CODE = 101
        fun newIntent(context: Context, productId: Long) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, productId)
            }
    }

    override val viewModel by inject<ProductDetailViewModel> {
        Log.d("아이디",intent.getLongExtra(PRODUCT_ID_KEY, -1).toString())
        parametersOf(
            intent.getLongExtra(PRODUCT_ID_KEY, -1)
        )
    }

    override fun getViewBinding(): ActivityProductDetailBinding =
        ActivityProductDetailBinding.inflate(layoutInflater)


    override fun observeData() = viewModel.productDetailStateLiveData.observe(this) {
        when(it){
            is ProductDetailState.UnInitialized -> initViews()
            is ProductDetailState.Loading -> handleLoading()
            is ProductDetailState.Success -> handleSuccess(it)
            is ProductDetailState.Error -> handleError()
            is ProductDetailState.Order -> handleOrder()

        }
    }

    private fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            Log.d("네비", "네비")
            toast("Click")
            finish()
        }

        orderButton.setOnClickListener {
            viewModel.orderProduct()
        }

    }

    private fun handleLoading() {
        binding.progressBar.isVisible = true
    }

    private fun handleSuccess(state: ProductDetailState.Success)= with(binding) {
        progressBar.isVisible=false
        val product = state.productEntity
        Log.d("product_name",product.productName)
        toolbar.title=product.productName
        setSupportActionBar(toolbar)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = product.productName

        productCategoryTextView.text = product.productType
        productImageView.loadCenterCrop(product.productImage,8f)
        productPriceTextView.text = "${product.productPrice} 원"
        introductionImageView.loadCenterCrop(product.productIntroductionImage)
    }

    private fun handleOrder() {
        toast("성공적으로 주문이 완료되었습니다.")
        finish()
    }

    private fun handleError() {
        toast("정보를 불러올 수 없습니다.")
        finish()
    }







}