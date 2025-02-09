package com.example.hospitalapplication.network

import com.example.hospitalapplication.models.ModelAllCalls
import com.example.hospitalapplication.models.ModelAllCases
import com.example.hospitalapplication.models.ModelAllEmployee
import com.example.hospitalapplication.models.ModelAllReports
import com.example.hospitalapplication.models.ModelAllTasks
import com.example.hospitalapplication.models.ModelCaseDetails
import com.example.hospitalapplication.models.ModelCreation
import com.example.hospitalapplication.models.ModelShowCall
import com.example.hospitalapplication.models.ModelShowMedicalRecordAnalysis
import com.example.hospitalapplication.models.ModelShowMedicalRecordDoctor
import com.example.hospitalapplication.models.ModelShowReport
import com.example.hospitalapplication.models.ModelTaskDetails
import com.example.hospitalapplication.models.ModelUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCalls {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email:String, @Field("password") password:String, @Field("device_token") deviceToken:String): ModelUser

    @FormUrlEncoded
    @POST("register")
    suspend fun register( @Field("email") email: String, @Field("password") password: String, @Field("first_name") fName: String,
                          @Field("last_name") lName: String, @Field("gender") gender: String, @Field("specialist") specialist: String,
                          @Field("birthday") birthday: String, @Field("status") status: String, @Field("address") address: String,
                          @Field("mobile") mobile: String, @Field("type") type: String): ModelUser

    @GET("doctors")
    suspend fun allUsers(@Query("type") type: String): ModelAllEmployee

    @FormUrlEncoded
    @POST("show-profile")
    suspend fun showProfile(@Field("user_id") userId: Int): ModelUser

    @GET("calls")
    suspend fun allCalls(@Query("created_at") date: String): ModelAllCalls

    @FormUrlEncoded
    @POST("calls")
    suspend fun createCallReceptionist(@Field("patient_name") name: String, @Field("age") age: String,
                                       @Field("doctor_id") doctorId: Int, @Field("phone") phone: String,
                                       @Field("description") description: String): ModelCreation

    @GET("calls/{id}")
    suspend fun showCallsDetails(@Path("id") id:Int) : ModelShowCall

    @PUT("calls/{id}")
    suspend fun logoutCall(@Path("id") id: Int): ModelCreation

    @PUT("calls-accept/{id}")
    suspend fun acceptRejectCall(@Path("id") id: Int, @Query("status") status: String): ModelCreation

    @GET("case")
    suspend fun getAllCases(): ModelAllCases

    @GET("case/{id}")
    suspend fun showCase(@Path("id") id: Int): ModelCaseDetails

    @FormUrlEncoded
    @POST("add-nurse")
    suspend fun addNurse (@Field("call_id") calId : Int,@Field("user_id") nurseId : Int ) : ModelCreation

    @FormUrlEncoded
    @POST("make-request")
    suspend fun requestAnalysis (@Field("call_id") callId : Int,@Field("user_id") userId : Int,@Field("note") note : String
                                 ,@Field("types[]") types : List<String> ) : ModelCreation

    @GET("reports")
    suspend fun getAllReports(@Query("date") date: String): ModelAllReports

    @DELETE("reports/{id}")
    suspend fun endReport(@Path("id") id: Int): ModelCreation

    @GET("reports/{id}")
    suspend fun showReport (@Path("id") id : Int ) : ModelShowReport

    @FormUrlEncoded
    @PUT("reports/{id}")
    suspend fun answerReport (@Path("id") id : Int, @Field("answer") answer :String) : ModelCreation

    @FormUrlEncoded
    @POST("reports")
    suspend fun createReport(
        @Field("report_name") reportName: String, @Field("description") description: String): ModelCreation

    @GET("tasks")
    suspend fun showAllTasks(@Query("date") date: String): ModelAllTasks

    @FormUrlEncoded
    @POST("tasks")
    suspend fun createTasks (@Field("user_id") userId : Int,@Field("task_name") taskName  :String,@Field("description") description :String
                             ,@Field("todos[]") todoList : List<String> ) : ModelCreation
    @FormUrlEncoded
    @PUT("tasks/{id}")
    suspend fun execution(
        @Path("id") id: Int, @Field("note") note: String): ModelCreation

    @GET("tasks/{id}")
    suspend fun showTask(@Path("id") id: Int): ModelTaskDetails

    @FormUrlEncoded
    @POST("measurement")
    suspend fun uploadMeasurement (@Field("call_id") caseId : Int,@Field("blood_pressure") bloodPressure :String,@Field("sugar_analysis") sugarAnalysis :String
                                   ,@Field("tempreture") tempreture :String,@Field("fluid_balance") fluidBalance :String,@Field("respiratory_rate") respiratoryRate :String
                                   ,@Field("heart_rate") heartRate :String,@Field("note") not :String,@Field("status") status : String ) : ModelCreation

    @FormUrlEncoded
    @POST("send-notification")
    suspend fun sendNotification (@Field("to") userId  : Int,@Field("title") title : String,@Field("body") body : String) : ModelCreation

    @FormUrlEncoded
    @POST("medical-record-show")
    suspend fun showMedicalRecordAnalysis (@Field("call_id") caseId  : Int) : ModelShowMedicalRecordAnalysis


    @FormUrlEncoded
    @POST("calls-manger")
    suspend fun sendCallManager (@Field("user_id") userId : Int,@Field("description")description :String ) : ModelCreation

    @Multipart
    @POST("medical-record")
    suspend fun uploadMedicalRecord (@Part part: MultipartBody.Part,@Part("call_id") callId: RequestBody,@Part("status") c: RequestBody,@Part("note") note: RequestBody) : ModelCreation

    @FormUrlEncoded
    @POST("attendance")
    suspend fun attendance(@Field("status") status: String): ModelCreation
}