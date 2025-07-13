package com.example.doctorbooking.controller

import com.example.doctorbooking.dto.AppointmentResponse
import com.example.doctorbooking.dto.CreateAppointmentRequest
import com.example.doctorbooking.service.AppointmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Appointment management endpoints")
class AppointmentController(private val appointmentService: AppointmentService) {
    
    @PostMapping
    @Operation(summary = "Book an appointment")
    @PreAuthorize("hasRole('PATIENT')")
    fun bookAppointment(
        @RequestBody request: CreateAppointmentRequest,
        principal: Principal
    ): ResponseEntity<AppointmentResponse> {
        val patientId = principal.name.toLongOrNull() ?: throw IllegalArgumentException("Invalid user ID")
        return ResponseEntity.ok(appointmentService.bookAppointment(patientId, request.slotId))
    }
    
    @GetMapping("/my-appointments")
    @Operation(summary = "Get current user's appointments")
    @PreAuthorize("hasRole('PATIENT')")
    fun getMyAppointments(principal: Principal): ResponseEntity<List<AppointmentResponse>> {
        val patientId = principal.name.toLongOrNull() ?: throw IllegalArgumentException("Invalid user ID")
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId, null, null))
    }
    
    @PostMapping("/{appointmentId}/cancel")
    @Operation(summary = "Cancel an appointment")
    @PreAuthorize("hasRole('PATIENT')")
    fun cancelAppointment(
        @PathVariable appointmentId: Long,
        principal: Principal
    ): ResponseEntity<AppointmentResponse> {
        val patientId = principal.name.toLongOrNull() ?: throw IllegalArgumentException("Invalid user ID")
        return ResponseEntity.ok(appointmentService.cancelAppointment(patientId, appointmentId))
    }
    
    @GetMapping("/{appointmentId}")
    @Operation(summary = "Get appointment details")
    @PreAuthorize("hasRole('PATIENT')")
    fun getAppointmentDetails(
        @PathVariable appointmentId: Long,
        principal: Principal
    ): ResponseEntity<AppointmentResponse> {
        val patientId = principal.name.toLongOrNull() ?: throw IllegalArgumentException("Invalid user ID")
        return ResponseEntity.ok(appointmentService.getAppointmentDetails(patientId, appointmentId))
    }
} 