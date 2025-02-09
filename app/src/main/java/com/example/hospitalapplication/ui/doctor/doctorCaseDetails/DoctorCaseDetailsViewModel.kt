package com.example.hospitalapplication.ui.doctor.doctorCaseDetails

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
class DoctorCaseDetailsViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val showCaseLiveData = MutableLiveData<NetworkState>()
    fun showCase(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showCase(id)
                if (data.status == 1) {
                    showCaseLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    showCaseLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

   val addNurseLiveData = MutableLiveData<NetworkState>()
    fun addNurse(callId : Int , nurseId  :Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.addNurse(callId , nurseId)
                if (data.status == 1) {
                    addNurseLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    addNurseLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}