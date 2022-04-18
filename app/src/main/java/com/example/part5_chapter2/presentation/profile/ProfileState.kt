package com.example.part5_chapter2.presentation.profile

import android.net.Uri
import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.google.android.gms.auth.api.credentials.IdToken

sealed class ProfileState {
    object Uninitialized : ProfileState()
    object Loading : ProfileState()

    data class Login(val idToken: String) : ProfileState()

    sealed class Success : ProfileState() {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
            val productList: List<ProductEntity> = listOf()
        ):Success()

        object NotRegistered:Success()
    }

    object Error: ProfileState()


}