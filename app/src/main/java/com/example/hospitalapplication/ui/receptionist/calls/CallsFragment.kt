package com.example.hospitalapplication.ui.receptionist.calls

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
import com.example.hospitalapplication.adapters.AdapterCalls
import com.example.hospitalapplication.databinding.FragmentCallsBinding
import com.example.hospitalapplication.models.CallsData
import com.example.hospitalapplication.models.ModelAllCalls
import com.example.hospitalapplication.network.NetworkState
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar


@AndroidEntryPoint
class CallsFragment:Fragment() {
    private var _binding: FragmentCallsBinding?= null
    val binding get() = _binding!!
    private val adapter : AdapterCalls by lazy { AdapterCalls() }
    private val callsViewModel: CallsViewModel by viewModels()
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd")
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val cal: Calendar = Calendar.getInstance()
    private var date = ""
    private var id = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calls , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCallsBinding.bind(view)
        backArrowClick()
        initView()
        bindDataOnDataChanged()
        addCallClick()
        calenderClick()
        caseClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun addCallClick(){
        binding.add.setOnClickListener {
            findNavController().navigate(CallsFragmentDirections.actionCallsFragmentToCreateCallFragment())
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
        callsViewModel.allCallsData(date)
    }
    private fun caseClick(){
        adapter.setOnClick {
            this@CallsFragment.id = it
            findNavController().navigate(CallsFragmentDirections.actionCallsFragmentToCaseDetailsFragment(this@CallsFragment.id))
        }
    }
    private fun bindDataOnDataChanged() {
        callsViewModel.callsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllCalls
                    adapter.data = valuesReturned.data as ArrayList<CallsData>
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
            callsViewModel.filterItems(date)
        }
    }
    private fun bindDataOnDateChanged(){
        callsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
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