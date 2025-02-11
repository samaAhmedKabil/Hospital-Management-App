package com.example.myapplicationrubbish.ui.hr.hrScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentHrScreenBinding
import com.example.hospitalapplication.utils.MySharedPreferences

class HrScreenFragment: Fragment() {
    private var _binding: FragmentHrScreenBinding?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hr_screen , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHrScreenBinding.bind(view)
        onProfileClick()
        onEmployeeClick()
        onReportsClick()
        onTasksClick()
        fillInData()
        onAttendClick()
    }
    private fun onProfileClick(){
        binding.profilePic.setOnClickListener {
            findNavController().navigate(HrScreenFragmentDirections.actionHrHomeFragmentToProfileFragment(MySharedPreferences.getUserId()))
        }
    }
    private fun onEmployeeClick(){
        binding.blue.setOnClickListener {
            findNavController().navigate(R.id.employeeFragment)
        }
    }
    private fun onReportsClick(){
        binding.purple.setOnClickListener {
            findNavController().navigate(R.id.reportsFragment)
        }
    }
    private fun onTasksClick(){
        binding.green.setOnClickListener {
            findNavController().navigate(R.id.tasksFragment)
        }
    }
    private fun fillInData(){
        binding.type.text = MySharedPreferences.getUserType()
        binding.userName.text = MySharedPreferences.getUserName()
    }
    private fun onAttendClick() {
        binding.skyBlue.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }
    }
}
