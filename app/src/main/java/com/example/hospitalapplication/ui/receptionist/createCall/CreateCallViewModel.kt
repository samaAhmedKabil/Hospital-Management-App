package com.example.hospitalapplication.ui.receptionist.createCall

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
class CreateCallViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel(){
    val createCallLiveData = MutableLiveData<NetworkState>()

    fun createCall(name :String, age : String, doctorId : Int
                   , phone :String, description :String) {
        createCallLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.createCallReceptionist(name,age,doctorId,phone,description)
                if (data.status == 1) {
                    createCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    createCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}