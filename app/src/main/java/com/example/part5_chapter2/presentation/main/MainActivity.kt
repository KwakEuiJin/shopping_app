package com.example.part5_chapter2.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private val productListFragment:ProductListFragment by lazy {
        ProductListFragment()
    }
    private val profileFragment:ProfileFragment by lazy {
        ProfileFragment()
    }

    override val viewModel by inject<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        replaceFragment(ProductListFragment())
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_products -> {
                    replaceFragment(productListFragment)
                    true
                }

                R.id.menu_profile ->{
                    replaceFragment(profileFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.apply {
            beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commitAllowingStateLoss()
        }
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this){
        when(it){
            is MainState.RefreshOrderList ->{
                binding.bottomNav.selectedItemId = R.id.menu_profile
                replaceFragment(profileFragment)
                val fragment = ProfileFragment()
                (fragment as? BaseFragment<*, *>)?.viewModel?.fetchData()
            }
        }

    }






}