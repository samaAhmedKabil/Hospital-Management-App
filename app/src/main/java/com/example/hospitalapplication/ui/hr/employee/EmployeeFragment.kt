package com.example.myapplicationrubbish.ui.hr.employee

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterEmployeeHorizontalRecycler
import com.example.hospitalapplication.adapters.AdapterEmployeeVerticalRecycler
import com.example.hospitalapplication.databinding.FragmentEmployeeBinding
import com.example.hospitalapplication.models.EmployeeData
import com.example.hospitalapplication.models.ModelAllEmployee
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.ui.hr.employee.EmployeeViewModel
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeFragment:Fragment() {
    private var _binding: FragmentEmployeeBinding?= null
    private val binding get() = _binding!!
    private val data = arrayOf("All" , "Doctor" , "Nurse" , "HR" , "Analysis" , "Manager" , "Receptionist")
    private val adapter = AdapterEmployeeHorizontalRecycler(data)
    private val verticalAdapter : AdapterEmployeeVerticalRecycler by lazy { AdapterEmployeeVerticalRecycler() }
    private val employeeViewModel:EmployeeViewModel by viewModels()
    private var type = "All"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_employee , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEmployeeBinding.bind(view)
        binding.horizontalRecycler.adapter = adapter
        employeeViewModel.allUsersData(type)
        onAddClick()
        backArrowClick()
        bindDataOnDataChanged()
        onItemHorizontalClick()
        onItemVerticalClick()
        bindDataOnSearchChanged()
    }

    private fun onAddClick(){
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.newUserFragment)
        }
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindDataOnSearchChanged(){
        employeeViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            verticalAdapter.updateList(items)
        })
        search()
    }

    private fun bindDataOnDataChanged() {
        employeeViewModel.employeeLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllEmployee
                    verticalAdapter.data = valuesReturned.data as ArrayList<EmployeeData>
                    binding.recyclerDoctors.adapter = verticalAdapter
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun onItemHorizontalClick(){
        adapter.setOnClick {
            this@EmployeeFragment.type = it
            employeeViewModel.allUsersData(this@EmployeeFragment.type)
        }
    }

    private fun onItemVerticalClick(){
        verticalAdapter.setOnClick {
            findNavController().navigate(EmployeeFragmentDirections.actionEmployeeFragmentToMyProfileFragment(it))
        }
    }

    private fun search() {

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                employeeViewModel.filterItems(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
