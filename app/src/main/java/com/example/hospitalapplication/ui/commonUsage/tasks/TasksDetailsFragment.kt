package com.example.hospitalapplication.ui.commonUsage.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterToDo
import com.example.hospitalapplication.databinding.FragmentTasksDetailsBinding
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.models.ModelTaskDetails
import com.example.hospitalapplication.models.ToDo
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksDetailsFragment:Fragment() {
    private var _binding  : FragmentTasksDetailsBinding?= null
    private val binding get() = _binding!!
    private val adapterRecyclerToDo: AdapterToDo by lazy { AdapterToDo() }
    private val taskViewModel : TasksViewModel by viewModels()
    private var taskId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_details , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTasksDetailsBinding.bind(view)
        taskId = TasksDetailsFragmentArgs.fromBundle(requireArguments()).id
        taskViewModel.showTask(taskId)
        backArrowClick()
        executionClick()
        ifManager()
        bindDataOnDataChangedOfDetails()
        bindDataOnDataChangedOfExecution()
    }
    private fun ifManager() {
        binding.apply {
            if (MySharedPreferences.getUserType() == Const.MANAGER) {
                btnExecution.visibility = View.GONE
                editNoteTask.visibility = View.GONE
            }
        }
    }
    private fun backArrowClick(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun executionClick(){
        binding.btnExecution.setOnClickListener {
            taskViewModel.execution(taskId , binding.editNoteTask.text.toString())
        }
    }
    private fun bindDataOnDataChangedOfDetails(){
        taskViewModel.showTaskLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelTaskDetails
                    binding.apply {
                        data.data.apply {
                            textTaskName.text = task_name
                            textDate.text = created_at
                            textUserName.text = user.first_name + " " + user.last_name
                            textType.text = "Specialist - ${user.specialist}"
                            if (MySharedPreferences.getUserType() == Const.MANAGER) {
                                textDetails.text = note
                            } else {
                                textDetails.text = description
                            }
                            adapterRecyclerToDo.list = to_do as ArrayList<ToDo>
                            recyclerTodo.adapter = adapterRecyclerToDo
                        }
                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun bindDataOnDataChangedOfExecution(){
        taskViewModel.executionLiveData.observe(viewLifecycleOwner) {
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