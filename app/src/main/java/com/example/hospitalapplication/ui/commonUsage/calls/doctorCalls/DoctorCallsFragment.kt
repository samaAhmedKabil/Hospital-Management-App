package com.example.hospitalapplication.ui.commonUsage.calls.doctorCalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AdapterRecyclerCallsDoctor
import com.example.hospitalapplication.databinding.FragmentDoctorCallsBinding
import com.example.hospitalapplication.models.CallsData
import com.example.hospitalapplication.models.ModelAllCalls
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorCallsFragment:Fragment() {
    private var _binding: FragmentDoctorCallsBinding?= null
    val binding get() = _binding!!
    private var status = ""
    private val viewModel  : DoctorCallsViewModel by viewModels()
    private val adapterRecyclerCallsDoctor : AdapterRecyclerCallsDoctor by lazy { AdapterRecyclerCallsDoctor() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_calls , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorCallsBinding.bind(view)
        bindDataOnDataChanged()
        onItemClick()
        viewModel.getCalls()
        backArrowClick()
    }
    private fun backArrowClick(){
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun bindDataOnDataChanged() {
        viewModel.callsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val valuesReturned = it.data as ModelAllCalls
                    adapterRecyclerCallsDoctor.data = valuesReturned.data as ArrayList<CallsData>
                    binding.recyclerDoctors.adapter = adapterRecyclerCallsDoctor
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }

        viewModel.acceptRejectCallLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation

                    showToast(data.message)
                    if (status == Const.ACCEPT_CALL){
                        findNavController().navigate(DoctorCallsFragmentDirections.actionDoctorCallsFragmentToDoctorCasesFragment())
                    }else {
                        findNavController().popBackStack()
                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }
    private fun onItemClick(){
        adapterRecyclerCallsDoctor.onUserClick = object : AdapterRecyclerCallsDoctor.OnUserClick{
            override fun onAccept(id: Int) {

                status = Const.ACCEPT_CALL
                viewModel.acceptRejectCall(id , Const.ACCEPT_CALL)
            }

            override fun onReject(id: Int) {
                status = Const.REJECT_CALL
                viewModel.acceptRejectCall(id , Const.REJECT_CALL)
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}