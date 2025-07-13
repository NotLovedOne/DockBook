package com.example.doctorbooking.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateAppointmentRequest(
    @field:NotNull(message = "Slot ID is required")
    val slotId: Long
)