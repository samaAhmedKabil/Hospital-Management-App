package com.example.hospitalapplication.ui.doctor.doctorScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentDoctorScreenBinding
import com.example.hospitalapplication.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorScreenFragment:Fragment() {
    private var _binding: FragmentDoctorScreenBinding?= null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_screen , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorScreenBinding.bind(view)
        profileClick()
        callsClick()
        casesClick()
        onReportsClick()
        onTasksClick()
        onAttendClick()
    }
    private fun profileClick(){
        binding.profilePic.setOnClickListener{
            findNavController().navigate(DoctorScreenFragmentDirections.actionDoctorScreenFragmentToMyProfileFragment(MySharedPreferences.getUserId()))
        }
    }
    private fun callsClick(){
        binding.blue.setOnClickListener{
            findNavController().navigate(DoctorScreenFragmentDirections.actionDoctorScreenFragmentToDoctorCallsFragment())
        }
    }
    private fun casesClick(){
        binding.brownCase.setOnClickListener{
            findNavController().navigate(DoctorScreenFragmentDirections.actionDoctorScreenFragmentToDoctorCasesFragment())
        }
    }
    private fun onReportsClick(){
        binding.purple.setOnClickListener {
            findNavController().navigate(DoctorScreenFragmentDirections.actionDoctorScreenFragmentToReportsFragment())
        }
    }
    private fun onTasksClick(){
        binding.green.setOnClickListener {
            findNavController().navigate(DoctorScreenFragmentDirections.actionDoctorScreenFragmentToTasksFragment())
        }
    }
    private fun onAttendClick() {
        binding.green.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }
    }
}