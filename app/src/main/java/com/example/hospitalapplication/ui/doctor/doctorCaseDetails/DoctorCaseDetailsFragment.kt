package com.example.hospitalapplication.ui.doctor.doctorCaseDetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterEmployeeHorizontalRecycler
import com.example.hospitalapplication.databinding.FragmentDoctorCaseDetailsBinding
import com.example.hospitalapplication.models.ModelCaseDetails
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorCaseDetailsFragment :Fragment() {
    private var _binding: FragmentDoctorCaseDetailsBinding?= null
    val binding get() = _binding!!
    private val data = arrayOf("Case" , "Medical Record" , "Medical Measurement")
    private val adapter = AdapterEmployeeHorizontalRecycler(data)
    private val viewModel  : DoctorCaseDetailsViewModel by viewModels()
    private var caseId = 0
    private var nurseName = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_case_details , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorCaseDetailsBinding.bind(view)
        binding.tabs.adapter = adapter
        caseId = DoctorCaseDetailsFragmentArgs.fromBundle(requireArguments()).id
        val bundle = Bundle().apply {
            putInt("caseId", caseId)
        }
        // Set result before showing dialog
        parentFragmentManager.setFragmentResult("requestKey", bundle)
        viewModel.showCase(caseId)
        bindDataOnDataChanged()
        backArrowClick()
        addNurseClick()
        nurseSelected()
        addRecordClick()
        adapterClicks()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun bindDataOnDataChanged() {
        viewModel.showCaseLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelCaseDetails
                    binding.layoutCaseDetails.apply {
                        valuesReturned.data.apply {
                            patientName.text = patient_name
                            txtAge.text = age + " " + "Years"
                            date.text = created_at
                            caseDes.text = description
                            txtStatus.text = status
                            txtPhone.text = phone
                            doctor.text = doctor_id
                            nurse.text = nurse_id
                            if (status == Const.STATUS_LOGOUT) {
                                statusImg.setImageResource(R.drawable.green_right)
                                logout.visibility = View.GONE
                            } else {
                                statusImg.setImageResource(R.drawable.orange_clock)
                                logout.visibility = View.VISIBLE
                            }
                        }
                    }
                    binding.layoutMedicalMeasurement.apply {
                        valuesReturned.data.apply {
                            textDate.text = created_at
                            textDetails.text = measurement_note
                            textBp.text = blood_pressure
                            textSuger.text = sugar_analysis
                            textTemp.text = tempreture
                            textFluidBalance.text = fluid_balance
                            textRespiratoryRate.text = respiratory_rate
                            textHeartRate.text = heart_rate
                            textUserName.text = nurse_id
                        }
                    }
                    binding.layoutMedicalRecord.apply {
                        valuesReturned.data.apply {
                            textDetails.text = medical_record_note
                            Glide.with(requireContext())
                                .load(image)
                                .error(R.drawable.orange_clock) // Optional: Show an error image if loading fails
                                .into(binding.layoutMedicalRecord.imageMedialRecord)
                            textUserName.text = analysis_id
                        }
                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun adapterClicks(){
        adapter.setOnClick {
            when(it){
                "Case" ->binding.apply {
                    layoutCaseDetails.parentCaseDetailsLayout.visibility = View.VISIBLE
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurement.visibility = View.GONE
                    layoutMedicalRecord.parentMedicalRecordLayout.visibility = View.GONE
                }
                "Medical Record" -> binding.apply {
                layoutCaseDetails.parentCaseDetailsLayout.visibility = View.GONE
                layoutMedicalMeasurement.parentLayoutMedicalMeasurement.visibility = View.GONE
                layoutMedicalRecord.parentMedicalRecordLayout.visibility = View.VISIBLE
            }
                "Medical Measurement" -> binding.apply {
                    layoutCaseDetails.parentCaseDetailsLayout.visibility = View.GONE
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurement.visibility = View.VISIBLE
                    layoutMedicalRecord.parentMedicalRecordLayout.visibility = View.GONE
                }

            }
        }
    }

    private fun addRecordClick() {
        binding.layoutCaseDetails.addRequest.setOnClickListener {
            val bottomSheetFragment = DoctorCaseDialog()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }
    private fun addNurseClick(){
        binding.layoutCaseDetails.addNurse.setOnClickListener {
            findNavController().navigate(DoctorCaseDetailsFragmentDirections.actionDoctorCaseDetailsFragmentToSelectDoctorFragment(Const.NURSE))
        }
    }
    private fun nurseSelected(){
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")?.observe(viewLifecycleOwner) {
            id -> if (id != 0 ) {
                    viewModel.addNurse(caseId, id)
                    findNavController().currentBackStackEntry?.savedStateHandle?.set("doctorId", 0)
                }
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")?.observe(viewLifecycleOwner) {
            result -> nurseName = result
            }

        viewModel.addNurseLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation
                    showToast(data.message)
                    binding.layoutCaseDetails.nurse.text = nurseName
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }
}



