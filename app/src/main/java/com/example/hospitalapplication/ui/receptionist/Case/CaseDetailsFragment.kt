package com.example.hospitalapplication.ui.receptionist.Case

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentCaseDetailsBinding
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.models.ModelShowCall
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaseDetailsFragment : Fragment() {
    private var _binding: FragmentCaseDetailsBinding?= null
    private val binding get() = _binding!!
    private val caseDetailsViewModel : CaseDetailsViewModel by viewModels()
    var caseId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_case_details , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCaseDetailsBinding.bind(view)
        caseId = CaseDetailsFragmentArgs.fromBundle(requireArguments()).id
        backArrowClick()
        caseDetailsViewModel.showCall(caseId)
        bindDataOnDataChanged()
        bindDataOnLogoutChanged()
        logoutClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun bindDataOnDataChanged() {
        caseDetailsViewModel.showCallLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelShowCall
                    binding.apply {
                        valuesReturned.data.apply {
                            binding.patientName.text = patient_name
                            binding.age.text = age + " " + "Years"
                            binding.date.text = created_at
                            binding.caseDes.text = description
                            binding.status.text = status
                            binding.phone.text = phone
                            if (status== Const.STATUS_LOGOUT){
                                binding.statusImg.setImageResource(R.drawable.green_right)
                                binding.logout.visibility = View.GONE
                            }else{
                                binding.statusImg.setImageResource(R.drawable.orange_clock)
                                binding.logout.visibility = View.VISIBLE
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
    private fun logoutClick(){
        binding.logout.setOnClickListener{
            caseDetailsViewModel.logoutCall(caseId)
        }
    }

    private fun bindDataOnLogoutChanged(){
        caseDetailsViewModel._logoutCallLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation
                    showToast(data.message)
                    findNavController().popBackStack()
                }
                else -> {
                    //showToast(it.msg)
                }
            }
        }
    }
}