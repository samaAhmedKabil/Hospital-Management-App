package com.example.hospitalapplication.ui.receptionist.createCall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentCreateCallReceptionistBinding
import com.example.hospitalapplication.models.CallsData
import com.example.hospitalapplication.models.ModelAllCalls
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.ui.receptionist.calls.CallsFragmentDirections
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCallFragment:Fragment() {
    private var _binding: FragmentCreateCallReceptionistBinding?= null
    private val binding get() = _binding!!
    private val receptionViewModel: CreateCallViewModel by viewModels()
    private var doctorId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_call_receptionist , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateCallReceptionistBinding.bind(view)
        selectDoctor()
        backArrowClick()
        sendCallClick()
        bindDataOnDataChanged()
    }
    private fun selectDoctor(){
        binding.selectADoctor.setOnClickListener{
            findNavController().navigate(CreateCallFragmentDirections.actionCreateCallFragmentToSelectDoctorFragment(Const.DOCTOR))
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                doctorId = result
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")?.observe(viewLifecycleOwner) { result ->
                binding.selectADoctor.text = result
            }
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun sendCallClick(){
        binding.sendCall.setOnClickListener{
            validateDataEntered()
        }
    }
    private fun bindDataOnDataChanged() {
        receptionViewModel.createCallLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation
                    findNavController().navigate(CreateCallFragmentDirections.actionCreateCallFragmentToCallSentSuccessfully())
                }
                else -> {
                    showToast("Success")
                }
            }
        }
    }
    private fun validateDataEntered(){
        val name = binding.patientName.text.toString()
        val age = binding.age.text.toString()
        val phone = binding.phone.text.toString()
        val description = binding.caseDes.text.toString()
        if (name.isEmpty()) {
            binding.patientName.error = getString(R.string.required)
        } else if (age.isEmpty()) {
            binding.age.error = getString(R.string.required)
        } else if (phone.isEmpty()) {
            binding.phone.error = getString(R.string.required)
        } else if (doctorId == 0) {
            showToast("Please Select Doctor")
        } else if (description.isEmpty()) {
            binding.caseDes.error = getString(R.string.required)
        } else {
            receptionViewModel.createCall(name, age, doctorId, phone, description)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}