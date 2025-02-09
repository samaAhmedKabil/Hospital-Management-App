package com.example.hospitalapplication.ui.analysis.caseDetailsAnalysis

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hospitalapplication.R
import com.example.hospitalapplication.adapters.AapterAnalysis
import com.example.hospitalapplication.adapters.AdapterEmployeeHorizontalRecycler
import com.example.hospitalapplication.databinding.FragmentCaseDetailsAnalysisBinding
import com.example.hospitalapplication.models.ModelCaseDetails
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.models.ModelShowMedicalRecordAnalysis
import com.example.hospitalapplication.network.NetworkState
import com.example.hospitalapplication.utils.Const
import com.example.myapplicationrubbish.utils.showToast
import com.google.android.gms.cast.framework.media.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class CaseDetailsAnalysisFragment: Fragment() {
    private var _binding: FragmentCaseDetailsAnalysisBinding?= null
    val binding get() = _binding!!
    private val caseDetailsAnalysisViewModel : CaseDetailsAnalysisViewModel by viewModels()
    private val adapterRecyclerAnalysis : AapterAnalysis by lazy { AapterAnalysis() }
    private val data = arrayOf("Case" , "Medical Record")
    private val adapter = AdapterEmployeeHorizontalRecycler(data)
    var caseId = 0
    private var images: List<Image>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_case_details_analysis , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCaseDetailsAnalysisBinding.bind(view)
        caseId = CaseDetailsAnalysisFragmentArgs.fromBundle(requireArguments()).id
        binding.tabs.adapter = adapter
        caseDetailsAnalysisViewModel.showCase(caseId)
        caseDetailsAnalysisViewModel.showMedicalRecordAnalysis(caseId)
        binding.layoutMedicalRecord.btnUploadImage.setOnClickListener {
            pickImageFromGallery()
        }
        backArrowClick()
        observers()
        adapterClicks()
    }
    private fun backArrowClick(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observers() {
        caseDetailsAnalysisViewModel.showCaseLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCaseDetails
                    binding.layoutMedicalRecord.apply {
                        data.data.apply {
                            textDate.text = created_at
                            textDetails.text = measurement_note
                            textUserName.text = doctor_id
                        }
                    }
                    binding.layoutCaseDetailsAnalysis.apply {
                        data.data.apply {
                            textPatientAge.text = age + " " + "Years"
                            textPatientDate.text = created_at
                            textPatientPhone.text = phone
                            textPatientName.text = patient_name
                            textPatientDesc.text = description
                            textPatientStatus.text = status
                            textPatientNurse.text = nurse_id
                            textPatientDoctor.text = doctor_id
                            if (status == Const.STATUS_LOGOUT) {
                                imageCondition.setImageResource(R.drawable.ic_check)
                            } else {
                                imageCondition.setImageResource(R.drawable.ic_delay)
                            }
                        }
                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }

        caseDetailsAnalysisViewModel.showMedicalRecordAnalysis.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelShowMedicalRecordAnalysis
                    binding.layoutMedicalRecord.apply {
                        data.data.apply {
                            textDetails.text = note
                            textUserName.text = user.first_name + " " + user.last_name
                            adapterRecyclerAnalysis.list = type as ArrayList<String>
                            recyclerAnalysis.adapter = adapterRecyclerAnalysis
                        }
                    }
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }

        caseDetailsAnalysisViewModel.uploadRecordAnalysis.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                }
                NetworkState.Status.SUCCESS -> {
                    val data = it.data as ModelCreation
                    showToast(data.message)
                }
                else -> {
                    showToast(it.msg)
                }
            }
        }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let {
                // Display image using Glide
                Glide.with(requireActivity()).load(it).into(binding.layoutMedicalRecord.uploadImage)
                // Convert URI to File and upload
                val imageFile = uriToFile(it)
                if (imageFile != null) {
                    prepareFilePart(imageFile)
                }
            }
        }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    // Function to convert URI to File
    private fun uriToFile(uri: Uri): File? {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "selected_image.png")

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return if (file.exists()) file else null
    }

    // Function to prepare file for multipart upload
    private fun prepareFilePart(imageFile: File) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val multiPartImage = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val caseIdBody: RequestBody = caseId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val note: RequestBody = "Done".toRequestBody("text/plain".toMediaTypeOrNull())
        val status: RequestBody = "Done".toRequestBody("text/plain".toMediaTypeOrNull())
        // Call ViewModel to upload the image
        caseDetailsAnalysisViewModel.uploadRecordAnalysis(multiPartImage, caseIdBody, note, status)
    }

    private fun adapterClicks(){
        adapter.setOnClick {
            when(it){
                "Case" ->binding.apply {
                    layoutCaseDetailsAnalysis.parentLayoutCaseDetailsAnalysis.visibility = View.VISIBLE
                    layoutMedicalRecord.parentLayoutMedicalRecord.visibility = View.GONE
                }
                "Medical Record" -> binding.apply {
                    layoutMedicalRecord.parentLayoutMedicalRecord.visibility = View.VISIBLE
                    layoutCaseDetailsAnalysis.parentLayoutCaseDetailsAnalysis.visibility = View.GONE
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}