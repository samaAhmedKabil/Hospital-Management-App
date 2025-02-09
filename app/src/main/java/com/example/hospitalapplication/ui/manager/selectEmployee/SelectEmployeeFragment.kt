package com.example.hospitalapplication.ui.manager.selectEmployee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterEmployeeHorizontalRecycler
import com.example.hospitalapplication.adapters.AdapterSelectDoctor
import com.example.hospitalapplication.databinding.FragmentSelectEmployeeBinding
import com.example.hospitalapplication.models.EmployeeData
import com.example.hospitalapplication.models.ModelAllEmployee
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.ui.hr.employee.EmployeeViewModel
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectEmployeeFragment: Fragment() {
    private var _binding: FragmentSelectEmployeeBinding?= null
    val binding get() = _binding!!
    private val data = arrayOf("All" , "Doctor" , "Nurse" , "HR" , "Analysis" , "Manager" , "Receptionist")
    private val adapter = AdapterEmployeeHorizontalRecycler(data)
    private val adapterSelectDoctor : AdapterSelectDoctor by lazy { AdapterSelectDoctor() }
    private val employeeViewModel: EmployeeViewModel by viewModels()
    private var type = "All"
    private var doctorId = 0
    private var doctorName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_employee , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectEmployeeBinding.bind(view)
        binding.recyclerTypesTaps.adapter = adapter
        employeeViewModel.allUsersData(type)
        backArrowClick()
        onItemHorizontalClick()
        onItemVerticalClick()
        onSelectClick()
        bindDataOnDataChanged()
    }

    private fun backArrowClick(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun onItemHorizontalClick(){
        adapter.setOnClick {
            this@SelectEmployeeFragment.type = it
            employeeViewModel.allUsersData(this@SelectEmployeeFragment.type)
        }
    }
    private fun onItemVerticalClick(){
        adapterSelectDoctor.onUserClick = object  : AdapterSelectDoctor.OnUserClick {
            override fun onClick(id: Int, name: String) {
                doctorId = id
                doctorName = name
            }
        }
    }
    private fun onSelectClick(){
        binding.btnSelectEmployee.setOnClickListener {
            if (doctorId ==0 ){
                showToast(getString(R.string.select_doctor_warn))
                return@setOnClickListener
            }
            findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorId", doctorId)
            findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorName", doctorName)
            findNavController().popBackStack()
        }
    }

    private fun bindDataOnDataChanged(){
        employeeViewModel.employeeLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelAllEmployee
                    adapterSelectDoctor.data = data.data as ArrayList<EmployeeData>
                    binding.recyclerEmployee.adapter = adapterSelectDoctor
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}