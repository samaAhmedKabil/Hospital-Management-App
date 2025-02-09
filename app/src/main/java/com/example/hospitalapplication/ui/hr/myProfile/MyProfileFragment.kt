package com.example.myapplicationrubbish.ui.hr.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.databinding.FragmentMyProfileBinding
import com.example.hospitalapplication.models.ModelUser
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.ui.hr.myProfile.MyProfileViewModel
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment:Fragment() {
    private var _binding: FragmentMyProfileBinding?= null
    val binding get() = _binding!!
    private var userId = 0
    private val profileViewModel : MyProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyProfileBinding.bind(view)
        userId = MyProfileFragmentArgs.fromBundle(requireArguments()).userId
        profileViewModel.getProfile(userId)
        observer()
        backArrowClick()
    }

    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observer (){

        profileViewModel.showProfileLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelUser
                    data.data.apply {
                        binding.name.text = "$first_name $last_name"
                        binding.locationText.text = address
                        binding.dateText.text = birthday
                        binding.emailText.text = email
                        binding.specializationText.text = specialist
                        binding.genderText.text = gender
                        binding.heartText.text = status.toString()
                        binding.phoneText.text = mobile

                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }
}