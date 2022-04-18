package com.example.part5_chapter2.presentation.list

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.widget.GridLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.part5_chapter2.databinding.FragmentProductListBinding
import com.example.part5_chapter2.databinding.FragmentProfileBinding
import com.example.part5_chapter2.extension.toast
import com.example.part5_chapter2.presentation.BaseFragment
import com.example.part5_chapter2.presentation.adapter.ProductListAdapter
import com.example.part5_chapter2.presentation.detail.ProductDetailActivity
import com.example.part5_chapter2.presentation.main.MainActivity
import com.example.part5_chapter2.presentation.profile.ProfileFragment
import com.example.part5_chapter2.presentation.profile.ProfileViewModel
import org.koin.android.ext.android.inject

internal class ProductListFragment :
    BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    companion object {
        const val TAG = "ProductListFragment"
    }

    override val viewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding =
        FragmentProductListBinding.inflate(layoutInflater)

    private val adapter = ProductListAdapter{
        val intent = ProductDetailActivity.newIntent(requireContext(),it.id)
        startProductDetailResult.launch(intent)
    }
    private val startProductDetailResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("result",result.toString())
            if (result.resultCode == ProductDetailActivity.PRODUCT_ORDER_RESULT_CODE){
                (requireActivity() as MainActivity).viewModel.refreshOrderList()
            } else{
                Log.d("result_code","코드가 다름")
            }

        }

    override fun observeData() = viewModel.productListStateLiveData.observe(this) {
        when (it) {
            is ProductListState.UnInitialized -> {
                initViews(binding)
            }
            is ProductListState.Loading -> {
                handleLoadingState()
            }
            is ProductListState.Success -> {
                handleSuccessState(it)
            }
            is ProductListState.Error -> {
                handleError()
            }
        }
    }

    private fun initViews(binding: FragmentProductListBinding) = with(binding) {
        Log.d("initViews","리스트")
        recyclerView.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity,2)
        recyclerView.layoutManager = gridLayoutManager
        Log.d("어댑터 초기화","어댑터 초기화")
        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding) {
        Log.d("Loading","리스트")

        refreshLayout.isRefreshing = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleSuccessState(state: ProductListState.Success) = with(binding) {

        Log.d("Success","리스트")
        refreshLayout.isRefreshing = false
        if (state.productList.isEmpty()) {
            emptyResultTextView.isVisible = true
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isVisible = false
            recyclerView.isGone = false
            adapter.submitList(state.productList)
            adapter.notifyDataSetChanged()
        }
    }


    private fun handleError() = with(binding) {
        refreshLayout.isRefreshing = true
    }


}