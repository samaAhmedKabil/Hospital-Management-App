package com.example.hospitalapplication.ui.doctor.MedicalRecord

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
class MedicalRecordViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    var requestAnalysisLiveData = MutableLiveData<NetworkState>()
    fun requestAnalysis(callId : Int,userId : Int, note : String, types : List<String> ) {
        requestAnalysisLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.requestAnalysis(callId,userId , note,  types)
                if (data.status == 1) {
                    requestAnalysisLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    requestAnalysisLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}