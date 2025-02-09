package com.example.hospitalapplication.ui.commonUsage.tasks

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
import com.example.hospitalapplication.adapters.AdapterTasks
import com.example.hospitalapplication.databinding.FragmentTasksBinding
import com.example.hospitalapplication.models.ModelAllReports
import com.example.hospitalapplication.models.ModelAllTasks
import com.example.hospitalapplication.models.TasksData
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class TasksFragment:Fragment() {
    private var _binding  : FragmentTasksBinding?= null
    private val binding get() = _binding!!
    private val adapter : AdapterTasks by lazy { AdapterTasks() }
    private val tasksViewModel: TasksViewModel by viewModels()
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd")
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val cal: Calendar = Calendar.getInstance()
    private var date = ""
    private var id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTasksBinding.bind(view)
        backArrowClick()
        initView()
        initViewOfManager()
        calenderClick()
        bindDataOnDataChanged()
        recyclerItemClick()
        onAddClick()
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun recyclerItemClick(){
        adapter.setOnClick {
            this@TasksFragment.id = it
            findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToTasksDetailsFragment(this@TasksFragment.id))
        }
    }
    private fun calenderClick(){
        binding.calender.setOnClickListener{
            dataPicker()
        }
        bindDataOnDateChanged()
    }
    private fun initView (){
        date = /*fullFormat.format(cal.time)*/ "2025-02-8"
        binding.dateContainer.text =date
        tasksViewModel.showAllTasks(date)
    }
    private fun initViewOfManager (){
        if (MySharedPreferences.getUserType() == Const.MANAGER)
            binding.add.visibility = View.VISIBLE
    }
    private fun onAddClick(){
        binding.add.setOnClickListener {
            findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToCreateTaskFragment())
        }
    }
    private fun bindDataOnDataChanged() {
        tasksViewModel.showAllTasksLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllTasks
                    adapter.data = valuesReturned.data as MutableList<TasksData>
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
            tasksViewModel.filterItems(date)
        }
    }
    private fun bindDataOnDateChanged(){
        tasksViewModel.items.observe(viewLifecycleOwner, Observer { items ->
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