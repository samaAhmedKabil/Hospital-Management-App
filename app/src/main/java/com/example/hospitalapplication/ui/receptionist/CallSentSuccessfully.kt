package com.example.hospitalapplication.ui.receptionist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentSuccessedCancelRequestBinding

class CallSentSuccessfully: Fragment() {
    private var _binding: FragmentSuccessedCancelRequestBinding?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_successed_cancel_request , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSuccessedCancelRequestBinding.bind(view)
        backHomeClick()
    }
    private fun backHomeClick(){
        binding.back.setOnClickListener{
            findNavController().navigate(CallSentSuccessfullyDirections.actionCallSentSuccessfullyToReceptionistScreenFragment())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}