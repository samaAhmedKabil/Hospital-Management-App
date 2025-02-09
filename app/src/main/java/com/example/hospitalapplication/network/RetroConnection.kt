package com.example.hospitalapplication.network

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class RetroConnection @Inject constructor(private val apiCalls: ApiCalls)  {
    suspend fun retroRegister(email: String, password: String, fName: String, lName: String, gender: String, specialist: String, birthday: String, status: String, address: String, mobile: String, type: String) = withContext(IO) {
        apiCalls.register(email, password, fName, lName, gender, specialist, birthday, status, address, mobile, type)
    }
    suspend fun retroLogin( email: String, password: String,deviceToken :String ) = withContext(IO) {
        apiCalls.login(email,password,deviceToken)
    }
    suspend fun allUsers (type : String)  = withContext(IO){
        apiCalls.allUsers(type)
    }
    suspend fun showProfile (userId : Int) = withContext(IO){
        apiCalls.showProfile(userId)
    }
    suspend fun allCalls (date : String) = withContext(IO){
        apiCalls.allCalls(date)
    }
    suspend fun createCallReceptionist ( name :String, age : String, doctorId : Int, phone :String, description :String)  = withContext(IO){
        apiCalls.createCallReceptionist(name,age,doctorId,phone,description)
    }
    suspend fun showCaseDetails (id : Int) = withContext(IO){
        apiCalls.showCallsDetails(id)
    }
    suspend fun logoutCall (id :Int) = withContext(IO){
        apiCalls.logoutCall(id)
    }
    suspend fun acceptRejectCall (id : Int, status :String) = withContext(IO){
        apiCalls.acceptRejectCall(id,status)
    }
    suspend fun getAllCases()= withContext(IO){
        apiCalls.getAllCases()
    }
    suspend fun showCase(id: Int)= withContext(IO){
        apiCalls.showCase(id)
    }
    suspend fun addNurse ( calId : Int, nurseId : Int ) = withContext(IO){
        apiCalls.addNurse(calId,nurseId)
    }
    suspend fun requestAnalysis ( callId : Int,userId : Int, note : String, types : List<String> ) = withContext(IO){
        apiCalls.requestAnalysis(callId,userId , note,  types)
    }
    suspend fun getAllReports (date  :String)  = withContext(IO){
        apiCalls.getAllReports(date)
    }

    suspend fun answerReport ( id : Int,  answer :String) = withContext(IO){
        apiCalls.answerReport(id, answer)
    }

    suspend fun endReport (id : Int) = withContext(IO){
        apiCalls.endReport(id)
    }
    suspend fun createReport (reportName : String,description :String) = withContext(IO){
        apiCalls.createReport(reportName, description)
    }
    suspend fun showReport (id : Int ) = withContext(IO){
        apiCalls.showReport(id)
    }
    suspend fun showAllTasks ( date :String) = withContext(IO){
        apiCalls.showAllTasks(date)
    }
    suspend fun execution (id : Int,note :String) = withContext(IO){
        apiCalls.execution(id,note)
    }
    suspend fun showTask ( id : Int)  = withContext(IO){
        apiCalls.showTask(id)
    }
    suspend fun createTasks ( userId : Int, taskName  :String, description :String, todoList : List<String> ) = withContext(IO){
        apiCalls.createTasks(userId,taskName,description,todoList)
    }
    suspend fun uploadMeasurement (caseId : Int,bloodPressure :String, sugarAnalysis :String, tempreture :String,fluidBalance :String, respiratoryRate :String, heartRate :String, note :String) = withContext(IO){
        apiCalls.uploadMeasurement(caseId,bloodPressure , sugarAnalysis,tempreture,fluidBalance ,respiratoryRate,heartRate, note, "done")
    }
    suspend fun sendNotification (userId  : Int,title : String, body : String)= withContext(IO){
        apiCalls.sendNotification(userId, title ,body)
    }
    suspend fun showMedicalRecordAnalysis ( caseId  : Int) = withContext(IO){
        apiCalls.showMedicalRecordAnalysis(caseId)
    }
    suspend fun uploadMedicalRecord ( part: MultipartBody.Part, call_id: RequestBody, status: RequestBody, note: RequestBody) = withContext(IO){
        apiCalls.uploadMedicalRecord(part, call_id , status , note)
    }
    suspend fun attendance (status : String ) = withContext(IO){
        apiCalls.attendance(status)
    }

}