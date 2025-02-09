package com.example.hospitalapplication.ui.nurse.nurseScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentNurseScreenBinding
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.ui.hr.hrScreen.HrScreenFragmentDirections

class NurseScreenFragment:Fragment() {
    private var _binding  : FragmentNurseScreenBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nurse_screen , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNurseScreenBinding.bind(view)
        onProfileClick()
        onCallsClick()
        onCasesClick()
        onReportsClick()
        onTasksClick()
        onAttendClick()
    }
    private fun onProfileClick(){
        binding.profilePic.setOnClickListener {
            findNavController().navigate(NurseScreenFragmentDirections.actionNurseScreenFragmentToMyProfileFragment(MySharedPreferences.getUserId()))
        }
    }
    private fun onCallsClick(){
        binding.blue.setOnClickListener {
            findNavController().navigate(R.id.doctorCallsFragment)
        }
    }
    private fun onCasesClick(){
        binding.brownCase.setOnClickListener {
            findNavController().navigate(R.id.doctorCasesFragment)
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
    private fun onAttendClick() {
        binding.green.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }
    }
}