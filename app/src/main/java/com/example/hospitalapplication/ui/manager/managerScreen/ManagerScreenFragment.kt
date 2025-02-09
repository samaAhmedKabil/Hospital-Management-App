package com.example.hospitalapplication.ui.manager.managerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentManagerScreenBinding
import com.example.hospitalapplication.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerScreenFragment:Fragment() {
    private var _binding  : FragmentManagerScreenBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manager_screen , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentManagerScreenBinding.bind(view)
        onProfileClick()
        onCasesClick()
        onEmployeesClick()
        onReportsClick()
        onTasksClick()
        onAttendClick()
    }
    private fun onProfileClick(){
        binding.profilePic.setOnClickListener {
            findNavController().navigate(ManagerScreenFragmentDirections.actionManagerScreenFragmentToMyProfileFragment(MySharedPreferences.getUserId()))
        }
    }
    private fun onCasesClick(){
        binding.blue.setOnClickListener {
            findNavController().navigate(R.id.doctorCasesFragment)
        }
    }
    private fun onEmployeesClick(){
        binding.brownCase.setOnClickListener {
            findNavController().navigate(R.id.employeeFragment)
        }
    }
    private fun onReportsClick(){
        binding.purple.setOnClickListener {
            findNavController().navigate(R.id.reportsFragment)
        }
    }
    private fun onTasksClick() {
        binding.green.setOnClickListener {
            findNavController().navigate(R.id.tasksFragment)
        }
    }
    private fun onAttendClick() {
        binding.green.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=  null
    }
}