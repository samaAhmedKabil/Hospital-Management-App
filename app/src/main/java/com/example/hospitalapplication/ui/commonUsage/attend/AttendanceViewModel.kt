package com.example.hospitalapplication.ui.commonUsage.attend

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
class AttendanceViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val attendanceLiveData = MutableLiveData<NetworkState>()
    fun makeAttendance(status: String ) {
        attendanceLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.attendance(status)

                if (data.status == 1) {
                    attendanceLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    attendanceLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}