package com.example.hospitalapplication.ui.nurse.caseDetails

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
class NurseCaseDetailsViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val showCaseLiveData = MutableLiveData<NetworkState>()
    fun showCase(id : Int) {
        showCaseLiveData.postValue(NetworkState.LOADING)
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

    val uploadMeasurementLiveData = MutableLiveData<NetworkState>()
    fun uploadMeasurement(caseId : Int,bloodPressure :String, sugarAnalysis :String, tempreture :String,fluidBalance :String, respiratoryRate :String, heartRate :String, note :String) {
        uploadMeasurementLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.uploadMeasurement(caseId,bloodPressure , sugarAnalysis
                    ,tempreture,fluidBalance ,respiratoryRate,heartRate, note)
                if (data.status == 1) {
                    uploadMeasurementLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    uploadMeasurementLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun sendNotification (userId  : Int,title : String, body : String){
        viewModelScope.launch(Dispatchers.IO) {
            retroConnection.sendNotification(userId, title, body)
        }
    }
}