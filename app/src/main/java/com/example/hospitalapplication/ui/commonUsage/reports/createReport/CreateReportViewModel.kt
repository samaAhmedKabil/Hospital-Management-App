package com.example.hospitalapplication.ui.commonUsage.reports.createReport

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
class CreateReportViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val createReportLiveData = MutableLiveData<NetworkState>()

    fun createReport(reportName: String, reportDescription : String) {
        createReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.createReport(reportName, reportDescription)
                if (data.status == 1) {
                    createReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    createReportLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}