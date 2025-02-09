package com.example.hospitalapplication.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.network.RetroConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val retroConnection: RetroConnection):ViewModel() {
     val loginLiveData = MutableLiveData<NetworkState>()
    fun login(email: String, password: String, deviceToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.retroLogin(email, password, deviceToken)
                if (data.status == 1){
                    loginLiveData.postValue(NetworkState.getLoaded(data))
                }
                else {
                    loginLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            }
            catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }
}