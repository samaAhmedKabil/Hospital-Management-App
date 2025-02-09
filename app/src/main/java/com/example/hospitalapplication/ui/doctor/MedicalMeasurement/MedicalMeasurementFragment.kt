package com.example.hospitalapplication.ui.doctor.MedicalMeasurement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.MedicalRandMAdapter
import com.example.hospitalapplication.databinding.LayoutMedicalMeasurementBinding
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicalMeasurementFragment:Fragment() {
    private var _binding: LayoutMedicalMeasurementBinding?= null
    val binding get() = _binding!!
    private val analysisList   = ArrayList<String>()
    private var analystId = 0
    private var caseId =  0
    private val adapterRecyclerMedical : MedicalRandMAdapter by lazy { MedicalRandMAdapter() }
    private val medicalMeasurementViewModel  : MedicalMeasurementViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_medical_measurement , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LayoutMedicalMeasurementBinding.bind(view)
        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
            caseId = bundle.getInt("caseId", 0) // Default -1 if missing
        }
        backArrowClick()
        medicalMeasurementAddClick()
        removeItem()
        bindDataOnDataChanged()
        sendClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun medicalMeasurementAddClick(){
        binding.addMeasurement.setOnClickListener {
            val analysis = binding.addMeasurement.text.toString()
            if (analysis == ""){
                binding.addMeasurement.error = getString(R.string.required)
                return@setOnClickListener
            }
            analysisList.add(analysis)
            adapterRecyclerMedical.data = analysisList
            binding.tabs.adapter = adapterRecyclerMedical
            binding.addMeasurement.setText("")
        }
    }
    private fun removeItem(){
        adapterRecyclerMedical.setOnClick {
            analysisList.remove(it)
            adapterRecyclerMedical.data = analysisList
            binding.tabs.adapter = adapterRecyclerMedical
        }
    }
    private fun bindDataOnDataChanged(){
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")?.observe(viewLifecycleOwner) {
            id -> analystId = id
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")?.observe(viewLifecycleOwner) {
            result -> binding.selectDoctorText.text = result
            }
        medicalMeasurementViewModel.requestAnalysisLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation
                    showToast(data.message)
                    findNavController().popBackStack()
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }
    private fun sendClick(){
        binding.sendBtn.setOnClickListener {
            if (analysisList.size == 0) {
                showToast(getString(R.string.analysis_warn))
                return@setOnClickListener
            } else if (analystId == 0 || caseId == 0) {
                showToast(getString(R.string.analysis_emp_warn))
                return@setOnClickListener
            }
            medicalMeasurementViewModel.requestAnalysis(caseId, analystId, binding.addNote.text.toString(), analysisList)
        }
        binding.addAnalyst.setOnClickListener {
            findNavController().navigate(MedicalMeasurementFragmentDirections.actionMedicalMeasurementFragmentToSelectDoctorFragment(Const.ANALYSIS))
        }
    }
}