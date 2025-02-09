package com.example.hospitalapplication.ui.hr.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalapplication.models.EmployeeData
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.network.RetroConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel  @Inject constructor(private val retroConnection: RetroConnection):ViewModel() {
    val employeeLiveData = MutableLiveData<NetworkState>()
    private val _items = MutableLiveData<List<EmployeeData>>()
    val items: LiveData<List<EmployeeData>> get() = _items
    fun allUsersData(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.allUsers(type)
                _items.postValue(data.data)
                if (data.status == 1) {
                    employeeLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    employeeLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun filterItems(query: String) {
        val originalList = _items.value ?: return
        val filteredList = originalList.filter {
            it.first_name.contains(query)
        }
        _items.value = filteredList
    }
}