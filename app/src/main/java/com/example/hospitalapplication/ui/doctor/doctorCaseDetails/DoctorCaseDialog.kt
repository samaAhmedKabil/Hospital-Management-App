package com.example.hospitalapplication.ui.doctor.doctorCaseDetails

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.Dialog1Binding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DoctorCaseDialog : BottomSheetDialogFragment() {
    private var _binding: Dialog1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Dialog1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.medicalRecord.setOnClickListener {
            binding.medicalRecord.setBackgroundResource(R.drawable.rectangle_selected)
            binding.mdIcon.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.defaultGreen)
            medicalRecordClick()
        }
        binding.medicalMeasurement.setOnClickListener {
            binding.medicalMeasurement.setBackgroundResource(R.drawable.rectangle_selected)
            binding.mmIcon.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.defaultGreen)
            medicalMeasurementClick()
        }
    }

    private fun medicalRecordClick() {
        val navController = findNavController()
        try {
            Handler().postDelayed({
                navController.navigate(R.id.medicalRecordFragment)
                dismiss()
            }, 1000)
        } catch (e: IllegalStateException) {
            Log.e("DoctorCaseDetailsDialog", "Navigation failed", e)
        }
    }
    private fun medicalMeasurementClick() {
        val navController = findNavController()
        try {
            Handler().postDelayed({
                navController.navigate(R.id.medicalMeasurementFragment)
                dismiss()
            }, 1000)
        } catch (e: IllegalStateException) {
            Log.e("DoctorCaseDetailsDialog", "Navigation failed", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}