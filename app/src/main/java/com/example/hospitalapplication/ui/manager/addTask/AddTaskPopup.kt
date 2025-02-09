package com.example.hospitalapplication.ui.manager.addTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.PopupAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTaskPopup: BottomSheetDialogFragment() {
    private var _binding  : PopupAddTaskBinding?= null
    private val binding get() = _binding!!
    var notifyAddTask  : NotifyAddTask ?= null

    companion object {
        fun newInstance() = AddTaskPopup()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_add_task , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = PopupAddTaskBinding.bind(view)
        onClicks()
    }

    private fun onClicks() {
        binding.apply {
            btnAddTask.setOnClickListener {
                val task = editReportDescription.text.toString().trim()
                if (task.isNotEmpty()) {
                    notifyAddTask?.taskCallBack(task)
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}