package com.example.hospitalapplication.ui.receptionist.selectDoctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterSelectDoctor
import com.example.hospitalapplication.databinding.FragmentSelectDoctorBinding
import com.example.hospitalapplication.models.EmployeeData
import com.example.hospitalapplication.models.ModelAllEmployee
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.ui.hr.employee.EmployeeViewModel
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectDoctorFragment:Fragment() {
    private var _binding: FragmentSelectDoctorBinding?= null
    private val binding get() = _binding!!
    private val adapter : AdapterSelectDoctor by lazy { AdapterSelectDoctor() }
    private var docId = 0
    private var docName = ""
    private val employeeViewModel : EmployeeViewModel by viewModels()
    var searchKey = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_doctor , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectDoctorBinding.bind(view)
        searchKey = SelectDoctorFragmentArgs.fromBundle(requireArguments()).searchKey
        binding.selectDoctorText.text = "Select $searchKey"
        binding.select.text = "Select $searchKey"
        binding.search.hint = "Search for a $searchKey"
        employeeViewModel.allUsersData(searchKey)
        backArrowClick()
        bindOnDataChanged()
        itemClick()
        selectDoctorClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun bindOnDataChanged(){
        employeeViewModel.employeeLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllEmployee
                    adapter.index = -1
                    adapter.data = valuesReturned.data as ArrayList<EmployeeData>
                    binding.recyclerDoc.adapter = adapter
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun itemClick(){
        adapter.onUserClick = object : AdapterSelectDoctor.OnUserClick {
            override fun onClick(id: Int, name: String) {
                docId = id
                docName = name
            }
        }
    }
    private fun selectDoctorClick(){
        binding.select.setOnClickListener{
            if (docId == 0){
                showToast("Please Select $searchKey !!")
                return@setOnClickListener
            }
            findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorId", docId)
            findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorName", docName)
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}