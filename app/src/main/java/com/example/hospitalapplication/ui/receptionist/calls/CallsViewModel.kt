package com.example.hospitalapplication.ui.receptionist.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalapplication.models.CallsData
import com.example.hospitalapplication.models.EmployeeData
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.network.RetroConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallsViewModel  @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val callsLiveData = MutableLiveData<NetworkState>()
    private val _items = MutableLiveData<List<CallsData>>()
    val items: LiveData<List<CallsData>> get() = _items
    fun allCallsData(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.allCalls(date)
                _items.postValue(data.data)
                if (data.status == 1) {
                    callsLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    callsLiveData.postValue(NetworkState.getErrorMessage(data.message))

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