package com.example.hospitalapplication.ui.manager.creteTask

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
class CreateTaskViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val createTaskLiveData = MutableLiveData<NetworkState>()
    fun createTask(userId : Int, taskName  :String, description :String, todoList : List<String> ) {
        createTaskLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.createTasks(userId,taskName,description,todoList)
                if (data.status == 1) {
                    createTaskLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    createTaskLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            }
            catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}