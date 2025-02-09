package com.example.hospitalapplication.ui.analysis.caseDetailsAnalysis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.network.RetroConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CaseDetailsAnalysisViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
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


    val showMedicalRecordAnalysis = MutableLiveData<NetworkState>()
    fun showMedicalRecordAnalysis(id : Int) {
        showMedicalRecordAnalysis.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showMedicalRecordAnalysis(id)
                if (data.status == 1) {
                    showMedicalRecordAnalysis.postValue(NetworkState.getLoaded(data))
                } else {
                    showMedicalRecordAnalysis.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    val uploadRecordAnalysis = MutableLiveData<NetworkState>()
    fun uploadRecordAnalysis(part: MultipartBody.Part, call_id: RequestBody, status: RequestBody, note: RequestBody) {
        uploadRecordAnalysis.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.uploadMedicalRecord(part, call_id, status, note)
                if (data.status == 1) {
                    uploadRecordAnalysis.postValue(NetworkState.getLoaded(data))
                } else {
                    uploadRecordAnalysis.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}