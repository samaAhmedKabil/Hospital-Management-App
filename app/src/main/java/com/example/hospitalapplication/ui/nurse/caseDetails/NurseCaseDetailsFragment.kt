package com.example.hospitalapplication.ui.nurse.caseDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterEmployeeHorizontalRecycler
import com.example.hospitalapplication.databinding.FragmentCaseDetailsNurseBinding
import com.example.hospitalapplication.models.ModelCaseDetails
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NurseCaseDetailsFragment: Fragment() {
    private var _binding: FragmentCaseDetailsNurseBinding?= null
    val binding get() = _binding!!
    private val viewModel  : NurseCaseDetailsViewModel by viewModels()
    private val data = arrayOf("Case" , "Medical Measurement")
    private val adapter = AdapterEmployeeHorizontalRecycler(data)
    private var doctorId : Int ?= null
    private var patientName : String ?= null
    private var caseId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_case_details_nurse , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCaseDetailsNurseBinding.bind(view)
        caseId = NurseCaseDetailsFragmentArgs.fromBundle(requireArguments()).id
        viewModel.showCase(caseId)
        binding.tabs.adapter = adapter
        backArrowClick()
        bindDataOnDataChangedOfDetails()
        bindDataOnDataChangedOfMeasurement()
        onCallDoctorClick()
        onAddMeasurementClick()
        adapterClicks()
    }

    private fun backArrowClick(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun bindDataOnDataChangedOfDetails() {
        viewModel.showCaseLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCaseDetails
                    doctorId = data.data.doc_id
                    patientName = data.data.patient_name
                    binding.layoutMedicalMeasurement.apply {
                        data.data.apply {
                            textDate.text = created_at
                            textDetails.text = measurement_note
                            textUserName.text = doctor_id
                        }
                    }
                    binding.layoutCaseDetailsNurse.apply {
                        data.data.apply {
                            textPatientAge.text = age + " " + "Years"
                            textPatientDate.text = created_at
                            textPatientPhone.text = phone
                            textPatientName.text = patient_name
                            textPatientDesc.text = description
                            textPatientStatus.text = status
                            textPatientNurse.text = nurse_id
                            textPatientDoctor.text = doctor_id
                            if (status == Const.STATUS_LOGOUT) {
                                imageCondition.setImageResource(R.drawable.done)
                            } else {
                                imageCondition.setImageResource(R.drawable.ic_delay)
                            }
                        }
                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun bindDataOnDataChangedOfMeasurement(){
        viewModel.uploadMeasurementLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation
                    showToast(data.message)
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun onCallDoctorClick(){
        binding.layoutCaseDetailsNurse.btnCallDoctor.setOnClickListener {
            if (doctorId == null)
                return@setOnClickListener
            viewModel.sendNotification(doctorId!! ,"Emergency", "Come to patient $patientName")
        }
    }

    private fun onAddMeasurementClick(){
        binding.layoutMedicalMeasurement.btnAddMeasurement.setOnClickListener{
            val suger = binding.layoutMedicalMeasurement.editSuger.text.toString()
            val bloodPressure = binding.layoutMedicalMeasurement.editBloodPressure.text.toString()
            val heartRate = binding.layoutMedicalMeasurement.editHeartRate.text.toString()
            val fluidBalance = binding.layoutMedicalMeasurement.editFluidBalance.text.toString()
            val respiratoryRate = binding.layoutMedicalMeasurement.editRespiratoryRate.text.toString()
            val temp = binding.layoutMedicalMeasurement.editTemp.text.toString()
            val note= binding.layoutMedicalMeasurement.editNoteMeasurement.text.toString()
            if (bloodPressure.isEmpty()){
                binding.layoutMedicalMeasurement.editBloodPressure.error = getString(R.string.required)
            }
            else if (suger.isEmpty()) {
                binding.layoutMedicalMeasurement.editSuger.error = getString(R.string.required)
            }
            else if (temp.isEmpty()){
                binding.layoutMedicalMeasurement.temp.error = getString(R.string.required)
            }
            else if (fluidBalance.isEmpty()){
                binding.layoutMedicalMeasurement.editFluidBalance.error = getString(R.string.required)
            }
            else if (respiratoryRate.isEmpty()) {
                binding.layoutMedicalMeasurement.editRespiratoryRate.error =
                    getString(R.string.required)
            }
            else if (heartRate.isEmpty()){
                binding.layoutMedicalMeasurement.editHeartRate.error = getString(R.string.required)
            }
            else{
                viewModel.uploadMeasurement(caseId,bloodPressure,suger,temp,fluidBalance,respiratoryRate,heartRate,note)
            }
        }
    }

    private fun adapterClicks(){
        adapter.setOnClick {
            when(it){
                "Case" ->binding.apply {
                    layoutCaseDetailsNurse.parentCaseDetailsNurse.visibility = View.VISIBLE
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurementNurse.visibility = View.GONE
                }
                "Medical Measurement" -> binding.apply {
                    layoutCaseDetailsNurse.parentCaseDetailsNurse.visibility = View.GONE
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurementNurse.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}