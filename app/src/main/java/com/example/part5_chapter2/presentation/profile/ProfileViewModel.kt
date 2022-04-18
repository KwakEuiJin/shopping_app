package com.example.part5_chapter2.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.part5_chapter2.data.preference.PreferenceManager
import com.example.part5_chapter2.domain.DeleteOrderedProductListUseCase
import com.example.part5_chapter2.domain.GetOrderedProductListUseCase
import com.example.part5_chapter2.presentation.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.getScopeId

internal class ProfileViewModel(
    private val preferenceManager: PreferenceManager,
    private val getOrderedProductListUseCase: GetOrderedProductListUseCase,
    private val deleteOrderedProductListUseCase: DeleteOrderedProductListUseCase
) :
    BaseViewModel() {

    private var _profileStateLiveData = MutableLiveData<ProfileState>(ProfileState.Uninitialized)
    val profileStateLiveData: LiveData<ProfileState> = _profileStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _profileStateLiveData.postValue(ProfileState.Loading)
        preferenceManager.getIdToken()?.let {
            _profileStateLiveData.postValue(ProfileState.Login(it))
        } ?: kotlin.run {
            _profileStateLiveData.postValue(ProfileState.Success.NotRegistered)
        }
    }



    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun signOut() = viewModelScope.launch {
        preferenceManager.removedToken()
        deleteOrderedProductListUseCase()
        fetchData()
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            _profileStateLiveData.postValue(
                ProfileState.Success.Registered(
                    user.displayName ?: "알수없음",
                    user.photoUrl,
                    getOrderedProductListUseCase()
                )
            )

        } ?: kotlin.run {
            _profileStateLiveData.postValue(
                ProfileState.Success.NotRegistered
            )
        }
    }
}