package com.example.hospitalapplication.ui.commonUsage.reports.reports.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalapplication.models.ReportsData
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.network.RetroConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsFragmentViewModel @Inject constructor(private val retroConnection: RetroConnection): ViewModel() {
    val reportsLiveData = MutableLiveData<NetworkState>()
    private val _items = MutableLiveData<List<ReportsData>>()
    val items: LiveData<List<ReportsData>> get() = _items


    fun getAllReports(date: String) {
        reportsLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.getAllReports(date)
                if (data.status == 1) {
                    reportsLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    reportsLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    val showReportLiveData = MutableLiveData<NetworkState>()

    fun showReport(id: Int) {
        showReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.showReport(id)
                if (data.status == 1) {
                    showReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    showReportLiveData.postValue(NetworkState.getErrorMessage(data.message))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    val endReportLiveData = MutableLiveData<NetworkState>()

    fun endReports(id: Int) {
        endReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retroConnection.endReport(id)
                if (data.status == 1) {
                    endReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    endReportLiveData.postValue(NetworkState.getErrorMessage(data.message))

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