package com.example.hospitalapplication.models

data class ModelAllEmployee(
    val `data`: List<EmployeeData>,
    val message: String,
    val status: Int
)

data class EmployeeData(
    val avatar: String,
    val first_name: String,
    val type: String,
    val id: Int
)