package com.example.hospitalapplication.ui.commonUsage.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalapplication.models.TasksData
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.network.RetroConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val showAllTasksLiveData = MutableLiveData<NetworkState>()
    private val _items = MutableLiveData<List<TasksData>>()
    val items: LiveData<List<TasksData>> get() = _items

    fun showAllTasks(date : String) {
        showAllTasksLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showAllTasks(date)
                if (data.status == 1) {
                    showAllTasksLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    showAllTasksLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


    val executionLiveData = MutableLiveData<NetworkState>()

    fun execution(id : Int , note : String) {
        executionLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.execution(id , note)
                if (data.status == 1) {
                    executionLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    executionLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


    val showTaskLiveData = MutableLiveData<NetworkState>()

    fun showTask(id : Int ) {
        showTaskLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showTask(id)
                if (data.status == 1) {
                    showTaskLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    showTaskLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun filterItems(query: String) {
        val originalList = _items.value ?: return
        val filteredList = originalList.filter {
            it.created_at.contains(query)
        }
        _items.value = filteredList
    }

}