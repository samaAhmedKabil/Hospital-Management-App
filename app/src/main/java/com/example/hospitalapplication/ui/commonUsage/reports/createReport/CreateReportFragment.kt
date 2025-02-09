package com.example.hospitalapplication.ui.commonUsage.reports.createReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentCreateReportBinding
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateReportFragment:Fragment() {
    private var _binding  : FragmentCreateReportBinding?= null
    private val binding get() = _binding!!
    private val createReportsViewModel  : CreateReportViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_report , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateReportBinding.bind(view)
        backArrowClick()
        onCreateClick()
        bindDataOnDataChanged()
    }

    private fun backArrowClick(){
        binding.arrowBack1.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindDataOnDataChanged(){
        createReportsViewModel.createReportLiveData.observe(viewLifecycleOwner) {
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

    private fun onCreateClick(){
        binding.create.setOnClickListener {
            validation()
        }
    }

    private fun validation (){
        val reportTittle = binding.reportName.text.toString()
        val reportDescription = binding.caseDes.text.toString()

        if (reportTittle.isEmpty()){
            binding.reportName.error = getString(R.string.required)
        }else if (reportDescription.isEmpty()){
            binding.caseDes.error = getString(R.string.required)
        }else{
            createReportsViewModel.createReport(reportTittle,reportDescription)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}