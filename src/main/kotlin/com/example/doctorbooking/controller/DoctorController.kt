package com.example.doctorbooking.controller

import com.example.doctorbooking.dto.CreateDoctorRequest
import com.example.doctorbooking.dto.CreateSlotRequest
import com.example.doctorbooking.dto.DoctorResponse
import com.example.doctorbooking.dto.SlotResponse
import com.example.doctorbooking.dto.AppointmentResponse
import com.example.doctorbooking.dto.UpdateAppointmentStatusRequest
import com.example.doctorbooking.dto.UpdateDoctorRequest
import com.example.doctorbooking.service.DoctorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Doctors", description = "Doctor management endpoints")
class DoctorController(private val doctorService: DoctorService) {
    @PostMapping
    @Operation(summary = "Create a new doctor")
    @PreAuthorize("hasRole('ADMIN')")
    fun createDoctor(@RequestBody request: CreateDoctorRequest): ResponseEntity<DoctorResponse> {
        return ResponseEntity.ok(doctorService.createDoctor(request))
    }

    @GetMapping
    @Operation(summary = "Get list of doctors")
    fun getDoctors(
        @RequestParam(required = false) specialty: String?,
        @RequestParam(required = false) available: Boolean?,
        @RequestParam(required = false) date: LocalDate?
    ): ResponseEntity<List<DoctorResponse>> {
        return ResponseEntity.ok(doctorService.getDoctors(specialty, available, date))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    fun getDoctor(@PathVariable id: Long): ResponseEntity<DoctorResponse> {
        return ResponseEntity.ok(doctorService.getDoctor(id))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor profile")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    fun updateDoctor(
        @PathVariable id: Long,
        @RequestBody request: UpdateDoctorRequest
    ): ResponseEntity<DoctorResponse> {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request))
    }

    @PostMapping("/{id}/slots")
    @Operation(summary = "Create slots for a doctor")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    fun createSlots(@PathVariable id: Long, @RequestBody request: CreateSlotRequest): ResponseEntity<SlotResponse> {
        return ResponseEntity.ok(doctorService.createSlot(id, request))
    }

    @GetMapping("/{id}/schedule")
    @Operation(summary = "Get available slots for a doctor")
    fun getSchedule(@PathVariable id: Long, @RequestParam date: LocalDate): ResponseEntity<List<SlotResponse>> {
        return ResponseEntity.ok(doctorService.getSchedule(id, date))
    }

    @GetMapping("/{id}/slots")
    @Operation(summary = "Get all slots for a doctor")
    fun getDoctorSlots(@PathVariable id: Long): ResponseEntity<List<SlotResponse>> {
        return ResponseEntity.ok(doctorService.getDoctorSlots(id))
    }

    @GetMapping("/{id}/appointments")
    @Operation(summary = "Get appointments for a doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    fun getDoctorAppointments(
        @PathVariable id: Long,
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) date: LocalDate?
    ): ResponseEntity<List<AppointmentResponse>> {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(id, status, date))
    }

    @PutMapping("/appointments/{appointmentId}/status")
    @Operation(summary = "Update appointment status")
    @PreAuthorize("hasRole('DOCTOR')")
    fun updateAppointmentStatus(
        @PathVariable appointmentId: Long,
        @RequestBody request: UpdateAppointmentStatusRequest
    ): ResponseEntity<AppointmentResponse> {
        return ResponseEntity.ok(doctorService.updateAppointmentStatus(appointmentId, request.status))
    }

    @DeleteMapping("/{id}/slots/{slotId}")
    @Operation(summary = "Delete a slot")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    fun deleteSlot(@PathVariable id: Long, @PathVariable slotId: Long): ResponseEntity<Void> {
        doctorService.deleteSlot(id, slotId)
        return ResponseEntity.noContent().build()
    }
} 