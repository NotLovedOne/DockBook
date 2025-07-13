package com.example.doctorbooking.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val role: String = "PATIENT"
)

data class UserResponse(
    val id: Long,
    val email: String,
    val role: String
)

data class UpdateAppointmentStatusRequest(
    val status: String
) 