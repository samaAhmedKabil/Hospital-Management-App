package com.example.hospitalapplication.ui.commonUsage.reports.reports.reports

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterReports
import com.example.hospitalapplication.databinding.FragmentReportsBinding
import com.example.hospitalapplication.models.ModelAllReports
import com.example.hospitalapplication.models.ReportsData
import com.example.hospitalapplication.network.NetworkState
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class ReportsFragment:Fragment() {
    private var _binding  : FragmentReportsBinding?= null
    private val binding get() = _binding!!
    private val adapter : AdapterReports by lazy { AdapterReports() }
    private val reportsViewModel: ReportsFragmentViewModel by viewModels()
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd")
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val cal: Calendar = Calendar.getInstance()
    private var date = ""
    private var id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reports , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReportsBinding.bind(view)
        backArrowClick()
        addReportClick()
        initView()
        calenderClick()
        bindDataOnDataChanged()
        recyclerItemClick()
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun recyclerItemClick(){
        adapter.setOnClick {
            this@ReportsFragment.id = it
            findNavController().navigate(ReportsFragmentDirections.actionReportsFragmentToReportDetailsFragment(this@ReportsFragment.id))
        }
    }
    private fun addReportClick(){
        binding.add.setOnClickListener {
            findNavController().navigate(ReportsFragmentDirections.actionReportsFragmentToCreateReportFragment())
        }
    }
    private fun calenderClick(){
        binding.calender.setOnClickListener{
            dataPicker()
        }
        bindDataOnDateChanged()
    }
    private fun initView (){
        date = fullFormat.format(cal.time)
        binding.dateContainer.text =date
        reportsViewModel.getAllReports(date)
    }
    private fun bindDataOnDataChanged() {
        reportsViewModel.reportsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllReports
                    adapter.data = valuesReturned.data as MutableList<ReportsData>
                    binding.callsRecycler.adapter = adapter
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun datePickerListener(){
        startDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val realMonth = month +1
            val  myMonth =  if (realMonth < 10) "0$realMonth" else realMonth.toString()
            val myday = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
            date = "${year}-${myMonth}-${myday}"
            binding.dateContainer.text = date
            reportsViewModel.filterItems(date)
        }
    }
    private fun bindDataOnDateChanged(){
        reportsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateList(items)
            datePickerListener()
        })

    }

    private fun dataPicker() {
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            startDateSetListener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}