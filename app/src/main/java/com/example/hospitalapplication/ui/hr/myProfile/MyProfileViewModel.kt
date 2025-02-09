package com.example.hospitalapplication.ui.hr.myProfile

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
class MyProfileViewModel @Inject constructor(private val retroConnection: RetroConnection):ViewModel() {

    private val _showProfileLiveData = MutableLiveData<NetworkState>()
    val showProfileLiveData get() = _showProfileLiveData
    fun getProfile(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showProfile(id)
                if (data.status == 1) {
                    _showProfileLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showProfileLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}