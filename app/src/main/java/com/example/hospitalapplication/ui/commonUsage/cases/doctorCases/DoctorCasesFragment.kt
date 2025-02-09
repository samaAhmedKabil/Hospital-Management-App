package com.example.hospitalapplication.ui.doctor.doctorCases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterDoctorCases
import com.example.hospitalapplication.databinding.FragmentCasesBinding
import com.example.hospitalapplication.models.CasesData
import com.example.hospitalapplication.models.ModelAllCases
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorCasesFragment:Fragment() {
    private var _binding: FragmentCasesBinding?= null
    val binding get() = _binding!!
    private val adapterRecyclerAllCases  : AdapterDoctorCases by lazy { AdapterDoctorCases() }
    private val viewModel  : DoctorCasesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cases , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCasesBinding.bind(view)
        viewModel.getCases()
        bindDataOnDataChanged()
        onItemClick()
        backArrowClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun bindDataOnDataChanged() {
        viewModel.casesLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllCases
                    adapterRecyclerAllCases.data = valuesReturned.data as ArrayList<CasesData>
                    binding.casesRecycler.adapter = adapterRecyclerAllCases
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }
    private fun onItemClick(){
        adapterRecyclerAllCases.onShowClick = object : AdapterDoctorCases.OnShowClick{
            override fun onShow(id: Int) {
                if (MySharedPreferences.getUserType() == Const.DOCTOR || MySharedPreferences.getUserType() == Const.MANAGER) {
                    findNavController().navigate(DoctorCasesFragmentDirections.actionDoctorCasesFragmentToDoctorCaseDetailsFragment(id))
                }
                else if (MySharedPreferences.getUserType() == Const.NURSE){
                    findNavController().navigate(DoctorCasesFragmentDirections.actionDoctorCasesFragmentToNurseCaseDetailsFragment(id))
                }
                else if (MySharedPreferences.getUserType() == Const.ANALYSIS){
                    findNavController().navigate(DoctorCasesFragmentDirections.actionDoctorCasesFragmentToCaseDetailsAnalysisFragment(id))
                }
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}