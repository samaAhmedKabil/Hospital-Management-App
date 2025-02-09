package com.example.hospitalapplication.ui.auth

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentLoginBinding
import com.example.hospitalapplication.models.ModelUser
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.hospitalapplication.utils.MySharedPreferences
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment:Fragment() {
    private var _binding: FragmentLoginBinding?= null
    private val loginViewModel: LoginViewModel by viewModels()
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        loginClick()
        showPassword()
        onDataChanged()
    }
    private fun loginClick(){
        binding.btnLogin.setOnClickListener{
            validate()
        }
    }

    private fun showPassword(){
        binding.showPass.setOnClickListener {
            if (binding.editText2.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.editText2.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.editText2.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun onDataChanged(){
        loginViewModel.loginLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelUser
                    MySharedPreferences.setUserEmail(valuesReturned.data.email)
                    MySharedPreferences.setUserTOKEN(valuesReturned.data.access_token)
                    MySharedPreferences.setUserName(valuesReturned.data.first_name + " " + valuesReturned.data.last_name)
                    MySharedPreferences.setUserPhone(valuesReturned.data.mobile)
                    MySharedPreferences.setUserType(valuesReturned.data.type)
                    MySharedPreferences.setUserId(valuesReturned.data.id)
                    navigationHandling(valuesReturned.data.type)
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private fun navigationHandling(speciality:String){
        if (speciality == Const.HR){
            findNavController().navigate(R.id.hrScreenFragment)
        }
        if (speciality == Const.RECEPTIONIST){
            findNavController().navigate(R.id.receptionistScreenFragment)
        }
        if (speciality == Const.DOCTOR){
            findNavController().navigate(R.id.doctorScreenFragment)
        }
        if (speciality == Const.NURSE){
            findNavController().navigate(R.id.nurseScreenFragment)
        }
        if (speciality == Const.ANALYSIS){
            findNavController().navigate(R.id.analysisScreenFragment)
        }
        if (speciality == Const.MANAGER){
            findNavController().navigate(R.id.managerScreenFragment)
        }
    }

    private fun validate(){
        val email = binding.editText.text.toString().trim()
        val password = binding.editText2.text.toString().trim()
        if (email.isEmpty()){
            binding.editText.error = getString(R.string.required)
        }
        else if (password.isEmpty()){
            binding.editText2.error = getString(R.string.required)
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editText.error = getString(R.string.wrong_email)
        }
        else{
            loginViewModel.login(email , password , "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
