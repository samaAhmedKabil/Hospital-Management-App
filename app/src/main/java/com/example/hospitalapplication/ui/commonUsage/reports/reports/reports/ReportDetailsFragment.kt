package com.example.hospitalapplication.ui.commonUsage.reports.reports.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentShowReportsBinding
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.models.ModelShowReport
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportDetailsFragment: Fragment() {
    private var _binding  : FragmentShowReportsBinding?= null
    private val binding get() = _binding!!
    private val reportsViewModel: ReportsFragmentViewModel by viewModels()
    private var id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_reports , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowReportsBinding.bind(view)
        id = ReportDetailsFragmentArgs.fromBundle(requireArguments()).id
        reportsViewModel.showReport(id)
        backArrowClick()
        hideMangerView()
        endClick()
        bindDataOnDataChanged()
        bindDataOnDataChangedOfEnd()
    }

    private fun backArrowClick(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun hideMangerView(){
        binding.apply {
            if (MySharedPreferences.getUserType() == Const.MANAGER) {
                textDate2.visibility = View.GONE
                cardView2.visibility = View.GONE
                textType2.visibility = View.GONE
                textDetailsManager.visibility = View.GONE
                btnEndReport.visibility = View.GONE
            } else {
                editReplyManager.visibility = View.GONE
                btnSendReplyReport.visibility = View.GONE
            }
        }
    }
    private fun endClick(){
        binding.btnEndReport.setOnClickListener {
            reportsViewModel.endReports(id)
        }
    }
    private fun bindDataOnDataChanged(){
        reportsViewModel.showReportLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelShowReport
                    binding.apply {
                        data.data.apply {
                            textReportName.text = report_name
                            textDate.text = created_at
                            textDetails.text = description
                            textUserName.text = user.first_name
                            textDate2.text = created_at
                            textDetailsManager.text = answer
                            textManagerName.text = manger.first_name + " " + manger.last_name
                            if (answer.isNotEmpty()) {
                                textDate2.visibility = View.VISIBLE
                                cardView2.visibility = View.VISIBLE
                                textType2.visibility = View.VISIBLE
                                textDetailsManager.visibility = View.VISIBLE
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

    private fun bindDataOnDataChangedOfEnd(){
        reportsViewModel.endReportLiveData.observe(viewLifecycleOwner) {
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
}