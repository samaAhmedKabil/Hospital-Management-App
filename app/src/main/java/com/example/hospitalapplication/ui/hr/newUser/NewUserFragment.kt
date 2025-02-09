package com.example.myapplicationrubbish.ui.hr.newUser

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentNewUserBinding
import com.example.hospitalapplication.models.ModelUser
import com.example.hospitalapplication.network.NetworkState
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class NewUserFragment:Fragment() {
    private var _binding: FragmentNewUserBinding? = null
    private val binding get() = _binding!!
    private val newUserViewModel: NewUserViewModel by viewModels()
    private var  birthDate = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewUserBinding.bind(view)
        dateClick()
        createUserClick()
        onDataChanged()
        backArrowClick()
    }

    private fun datePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            birthDate = formattedDate
            binding.date.text = birthDate
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun dateClick(){
        binding.date.setOnClickListener {
            datePicker()
        }
    }

    private fun onDataChanged(){
        newUserViewModel.newUserLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelUser
                    showToast(valuesReturned.message)
                    findNavController().popBackStack()
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun createUserClick(){
        binding.createUser.setOnClickListener{
            validate()
        }
    }

    private fun validate() {
        val fName = binding.firstName.text.toString()
        val lName = binding.lastName.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val address = binding.address.text.toString()
        val phone = binding.phone.text.toString()
        val gender = binding.gender.selectedItem.toString()
        var type = binding.specialist.selectedItem.toString()
        val status = binding.status.selectedItem.toString()
        if (fName.isEmpty()) {
            binding.firstName.error = getString(R.string.required)
        } else if (lName.isEmpty()) {
            binding.lastName.error = getString(R.string.required)
        } else if (binding.gender.selectedItemPosition == 0) {
            showToast(getString(R.string.gender_toast))
        } else if (binding.specialist.selectedItemPosition == 0) {
            showToast(getString(R.string.specialist_toast))
        } else if (birthDate.isEmpty()) {
            showToast(getString(R.string.date_toast))
        } else if (binding.status.selectedItemPosition == 0) {
            showToast(getString(R.string.status_toast))
        } else if (phone.isEmpty()) {
            binding.phone.error = getString(R.string.required)
        } else if (address.isEmpty()) {
            binding.address.error = getString(R.string.required)
        } else if (email.isEmpty()) {
            binding.email.error = getString(R.string.required)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = getString(R.string.wrong_email)
        } else if (password.isEmpty()) {
            binding.password.error = getString(R.string.required)
        } else {
            newUserViewModel.createUser(email,password,fName,lName,gender,type,birthDate,status,address,phone,type)
        }
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
