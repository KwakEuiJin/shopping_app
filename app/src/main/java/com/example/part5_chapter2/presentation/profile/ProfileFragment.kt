package com.example.part5_chapter2.presentation.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Adapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.part5_chapter2.R
import com.example.part5_chapter2.databinding.FragmentProfileBinding
import com.example.part5_chapter2.extension.loadCenterCrop
import com.example.part5_chapter2.extension.toast
import com.example.part5_chapter2.presentation.BaseFragment
import com.example.part5_chapter2.presentation.adapter.ProductListAdapter
import com.example.part5_chapter2.presentation.detail.ProductDetailActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import java.lang.Exception

internal class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    companion object {
        const val TAG = "ProfileFragment"
    }

    override val viewModel by inject<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.profileStateLiveData.observe(this) {
        Log.e("현재 상태",it.toString())
        when (it) {
            is ProfileState.Uninitialized -> initViews()
            is ProfileState.Loading -> handleLoadingState()
            is ProfileState.Login -> handleLoginState(it)
            is ProfileState.Success -> handleSuccessState(it)
            is ProfileState.Error -> handleErrorState()
        }
    }


    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("파베", result.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        Log.d("파이아베이스 아이디", account.id.toString())

                        viewModel.saveToken(account.idToken ?: throw Exception())

                    } ?: throw Exception()
                } catch (e: Exception) {
                    Log.e("에러", "loginLauncher ERROR")
                    e.printStackTrace()
                }
            }

        }

    private val adapter = ProductListAdapter{
        startActivity(
            ProductDetailActivity.newIntent(requireContext(),it.id)
        )
    }

    private fun initViews() = with(binding) {
        Log.d("initViews","프로필")
        recyclerView.adapter=adapter
        val gridLayoutManager = GridLayoutManager(activity,2)
        recyclerView.layoutManager = gridLayoutManager
        Log.d("어댑터 초기화","프로필")

        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    private fun signOut() {
        firebaseAuth.signOut()
        viewModel.signOut()
    }

    private fun handleLoadingState() = with(binding) {
        Log.d("Loading","프로필")
        progressBar.isVisible = true
        loginRequiredGroup.isGone = true

    }

    private fun handleLoginState(state: ProfileState.Login) = with(binding){
        Log.d("Login","프로필")
        Log.d("아이디 토큰",state.idToken)
        val credential = GoogleAuthProvider.getCredential(state.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Log.d("유저 정보 확인","")
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else{
                    task.addOnFailureListener {
                        Log.d("유저 정보 에러",it.toString())
                    }

                    viewModel.setUserInfo(null)
                }
            }

    }

    private fun handleSuccessState(state: ProfileState.Success) = with(binding) {
        progressBar.isGone = true
        when (state) {
            is ProfileState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is ProfileState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleRegisteredState(state: ProfileState.Success.Registered) = with(binding){
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.loadCenterCrop(state.profileImageUri.toString(),60f)
        userNameTextView.text=state.userName

        if (state.productList.isEmpty()){
            emptyResultTextView.isVisible = true
            recyclerView.isVisible = false
        } else{
            emptyResultTextView.isVisible = false
            recyclerView.isVisible = true
            Log.d("주문한 리스트",state.productList.toString())
            adapter.submitList(state.productList)
            adapter.notifyDataSetChanged()

        }

    }



    private fun handleErrorState() = with(binding) {
        requireActivity().toast("알 수 없는 에러발생")

    }



}