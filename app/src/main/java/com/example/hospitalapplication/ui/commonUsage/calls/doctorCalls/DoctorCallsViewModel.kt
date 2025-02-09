package com.example.hospitalapplication.ui.commonUsage.calls.doctorCalls

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
class DoctorCallsViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val callsLiveData = MutableLiveData<NetworkState>()
    fun getCalls() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.allCalls("")
                if (data.status == 1) {
                    callsLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    callsLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    val acceptRejectCallLiveData get() = MutableLiveData<NetworkState>()

    fun acceptRejectCall(id : Int,status : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.acceptRejectCall(id,status)
                if (data.status == 1) {
                    acceptRejectCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    acceptRejectCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}