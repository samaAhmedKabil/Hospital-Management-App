package com.example.hospitalapplication.ui.receptionist.receptionistScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentReseptionistScreenBinding
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.ui.hr.hrScreen.HrScreenFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceptionistScreenFragment:Fragment() {
    private var _binding: FragmentReseptionistScreenBinding?= null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reseptionist_screen , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReseptionistScreenBinding.bind(view)
        callsClick()
        onReportsClick()
        onTasksClick()
        profileClick()
        onAttendClick()
    }
    private fun callsClick(){
        binding.blue.setOnClickListener{
            findNavController().navigate(R.id.callsFragment)
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
    fun profileClick(){
        binding.profilePic.setOnClickListener{
            findNavController().navigate(ReceptionistScreenFragmentDirections.actionReceptionistScreenFragmentToMyProfileFragment(MySharedPreferences.getUserId()))
        }
    }
    private fun onAttendClick() {
        binding.green.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }
    }
}