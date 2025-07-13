package com.example.doctorbooking.controller

import com.example.doctorbooking.dto.AppointmentResponse
import com.example.doctorbooking.service.AppointmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patient management endpoints")
class PatientController(private val appointmentService: AppointmentService) {
    
    @GetMapping("/{patientId}/appointments")
    @Operation(summary = "Get appointments for a patient")
    @PreAuthorize("hasRole('PATIENT')")
    fun getPatientAppointments(
        @PathVariable patientId: Long,
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) date: LocalDate?
    ): ResponseEntity<List<AppointmentResponse>> {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId, status, date))
    }
    
    @PostMapping("/{patientId}/appointments/{appointmentId}/cancel")
    @Operation(summary = "Cancel an appointment")
    @PreAuthorize("hasRole('PATIENT')")
    fun cancelAppointment(@PathVariable patientId: Long, @PathVariable appointmentId: Long): ResponseEntity<AppointmentResponse> {
        return ResponseEntity.ok(appointmentService.cancelAppointment(patientId, appointmentId))
    }
    
    @GetMapping("/{patientId}/appointments/{appointmentId}")
    @Operation(summary = "Get specific appointment details")
    @PreAuthorize("hasRole('PATIENT')")
    fun getAppointmentDetails(@PathVariable patientId: Long, @PathVariable appointmentId: Long): ResponseEntity<AppointmentResponse> {
        return ResponseEntity.ok(appointmentService.getAppointmentDetails(patientId, appointmentId))
    }
} 