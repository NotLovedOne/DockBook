package com.example.doctorbooking.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateDoctorRequest(
    @field:NotBlank(message = "Email is required")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    val password: String,
    
    @field:NotBlank(message = "Full name is required")
    val fullName: String,
    
    @field:NotBlank(message = "Specialty is required")
    val specialty: String,
    
    val description: String? = null
)

data class UpdateDoctorRequest(
    val fullName: String? = null,
    val specialty: String? = null,
    val description: String? = null
)

data class DoctorResponse(
    val id: Long,
    val email: String,
    val fullName: String,
    val specialty: String,
    val description: String?
)

data class CreateSlotRequest(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

data class SlotResponse(
    val id: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val isBooked: Boolean
)

data class AppointmentResponse(
    val id: Long,
    val patientId: Long,
    val patientEmail: String,
    val doctorId: Long,
    val doctorName: String,
    val slotStartTime: LocalDateTime,
    val slotEndTime: LocalDateTime,
    val status: String
) 