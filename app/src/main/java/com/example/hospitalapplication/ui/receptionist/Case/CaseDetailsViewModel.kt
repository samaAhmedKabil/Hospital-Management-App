package com.example.hospitalapplication.ui.receptionist.Case

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
class CaseDetailsViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {

    val showCallLiveData = MutableLiveData<NetworkState>()

    fun showCall(id : Int) {
        showCallLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showCaseDetails(id)
                if (data.status == 1) {
                    showCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    showCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    val _logoutCallLiveData = MutableLiveData<NetworkState>()

    fun logoutCall(id : Int) {
        _logoutCallLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.logoutCall(id)
                if (data.status == 1) {
                    _logoutCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _logoutCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}