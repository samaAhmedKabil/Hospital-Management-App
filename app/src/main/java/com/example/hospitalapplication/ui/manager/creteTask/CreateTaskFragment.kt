package com.example.hospitalapplication.ui.manager.creteTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterCreateTask
import com.example.hospitalapplication.databinding.FragmentCaseDetailsNurseBinding
import com.example.hospitalapplication.databinding.FragmentCreateTaskBinding
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.ui.manager.addTask.AddTaskPopup
import com.example.hospitalapplication.ui.manager.addTask.NotifyAddTask
import com.example.hospitalapplication.ui.nurse.caseDetails.NurseCaseDetailsFragmentArgs
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskFragment:Fragment() , NotifyAddTask {
    private var _binding: FragmentCreateTaskBinding?= null
    val binding get() = _binding!!
    private val todoList = ArrayList<String>()
    private var userId = 0
    private var userName = ""
    private val adapterRecyclerCreateTask : AdapterCreateTask by lazy { AdapterCreateTask() }
    private val managerViewModel  : CreateTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_task , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateTaskBinding.bind(view)
        backArrowClick()
        onItemClick()
        onEditDoctorClick()
        onAddClick()
        onSendClick()
        bindDataOnDataChanged()
    }

    private fun backArrowClick(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun taskCallBack(task: String) {
        todoList.add(task)
        adapterRecyclerCreateTask.list = todoList
        binding.recyclerTasks.adapter = adapterRecyclerCreateTask
        showToast(getString(R.string.added))
    }

    private fun onItemClick() {
        adapterRecyclerCreateTask.onTaskDeleted = object : AdapterCreateTask.OnTaskDeleted {
            override fun onDelete(id: Int) {
                todoList.removeAt(id)
                adapterRecyclerCreateTask.list = todoList
                binding.recyclerTasks.adapter = adapterRecyclerCreateTask
            }
        }
    }

    private fun onEditDoctorClick(){
        binding.editDoctor.setOnClickListener {
            findNavController().navigate(CreateTaskFragmentDirections.actionCreateTaskFragmentToSelectEmployeeFragment())
        }
    }

    private fun onAddClick(){
        binding.btnAdd.setOnClickListener {
            val popupTask = AddTaskPopup.newInstance()
            popupTask.notifyAddTask = this@CreateTaskFragment
            popupTask.show(childFragmentManager , "task")
        }
    }

    private fun onSendClick(){
        binding.btnSendTask.setOnClickListener {
            val taskName = binding.editTaskName.text.toString().trim()
            val taskDescription  = binding.editReportDescription.text.toString()
            if (taskName.isEmpty()){
                binding.editTaskName.error = getString(R.string.required)
            }
            else if (userId == 0){
                showToast(getString(R.string.select_doctor_warn))
            }
            else if (taskDescription.isEmpty()){
                binding.editReportDescription.error = getString(R.string.required)
            }
            else if (todoList.size == 0){
                showToast(getString(R.string.add_tasks_warn))
            }
            else{
                managerViewModel.createTask(userId,taskName,taskDescription,todoList)
            }
        }
    }

    private fun bindDataOnDataChanged(){
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")?.observe(viewLifecycleOwner)
        { id -> userId = id }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")?.observe(viewLifecycleOwner)
            { result -> binding.editDoctor.text =  result }

        managerViewModel.createTaskLiveData.observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}