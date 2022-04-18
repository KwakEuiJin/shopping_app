package com.example.part5_chapter2.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.part5_chapter2.R
import com.example.part5_chapter2.databinding.ActivityMainBinding
import com.example.part5_chapter2.presentation.BaseActivity
import com.example.part5_chapter2.presentation.BaseFragment
import com.example.part5_chapter2.presentation.list.ProductListFragment
import com.example.part5_chapter2.presentation.profile.ProfileFragment
import com.google.android.gms.common.ErrorDialogFragment
import org.koin.android.ext.android.inject

internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel by inject<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        showFragment(ProductListFragment(),ProductListFragment.TAG)
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_products -> {
                    showFragment(ProductListFragment(),ProductListFragment.TAG)
                    true
                }

                R.id.menu_profile ->{
                    showFragment(ProfileFragment(),ProfileFragment.TAG)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }

    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this){
        when(it){
            is MainState.RefreshOrderList ->{
                Log.d("주문 완료","주문 완료")
                binding.bottomNav.selectedItemId = R.id.menu_profile
                showFragment(ProfileFragment(),ProfileFragment.TAG)
                val fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)
                (fragment as? BaseFragment<*, *>)?.viewModel?.fetchData()
            }
        }

    }






}