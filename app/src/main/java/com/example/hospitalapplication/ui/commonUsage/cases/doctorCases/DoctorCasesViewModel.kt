package com.example.hospitalapplication.ui.doctor.doctorCases

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
class DoctorCasesViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val casesLiveData = MutableLiveData<NetworkState>()
    fun getCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.getAllCases()
                if (data.status == 1) {
                    casesLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    casesLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}