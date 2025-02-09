package com.example.myapplicationrubbish.ui.hr.newUser

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
class NewUserViewModel @Inject constructor(private val retroConnection: RetroConnection):ViewModel() {
    val newUserLiveData = MutableLiveData<NetworkState>()
    fun createUser(email: String, password: String, fName: String, lName: String, gender: String, specialist: String, birthday: String, status: String, address: String, mobile: String, type: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = retroConnection.retroRegister(email, password, fName, lName, gender, specialist, birthday, status, address, mobile, type)
                if (data.status == 1){
                    newUserLiveData.postValue(NetworkState.getLoaded(data))
                }
                else {
                    newUserLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            }
            catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

}