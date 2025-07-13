package com.example.doctorbooking.service

import com.example.doctorbooking.dto.AppointmentResponse
import com.example.doctorbooking.entity.*
import com.example.doctorbooking.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class AppointmentService(
    private val appointmentRepository: AppointmentRepository,
    private val doctorSlotRepository: DoctorSlotRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun bookAppointment(patientId: Long, slotId: Long): AppointmentResponse {
        val patient = userRepository.findById(patientId).orElseThrow { IllegalArgumentException("Patient not found") }
        val slot = doctorSlotRepository.findById(slotId).orElseThrow { IllegalArgumentException("Slot not found") }
        
        if (slot.isBooked) {
            throw IllegalArgumentException("Slot is already booked")
        }
        
        if (slot.startTime.isBefore(LocalDateTime.now())) {
            throw IllegalArgumentException("Cannot book appointments in the past")
        }
        
        slot.isBooked = true
        doctorSlotRepository.save(slot)
        
        val appointment = appointmentRepository.save(
            Appointment(
                patient = patient,
                slot = slot,
                status = AppointmentStatus.SCHEDULED
            )
        )
        
        return AppointmentResponse(
            appointment.id!!,
            patient.id!!,
            patient.email,
            slot.doctor.id!!,
            slot.doctor.fullName,
            slot.startTime,
            slot.endTime,
            appointment.status.name
        )
    }
    
    fun getPatientAppointments(patientId: Long, status: String?, date: LocalDate?): List<AppointmentResponse> {
        val appointments = when {
            status != null && date != null -> {
                appointmentRepository.findByPatientIdAndStatusAndDate(patientId, AppointmentStatus.valueOf(status.uppercase()), date.atStartOfDay(), date.plusDays(1).atStartOfDay())
            }
            status != null -> {
                appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.valueOf(status.uppercase()))
            }
            date != null -> {
                appointmentRepository.findByPatientIdAndDate(patientId, date.atStartOfDay(), date.plusDays(1).atStartOfDay())
            }
            else -> {
                appointmentRepository.findByPatientId(patientId)
            }
        }
        
        return appointments.map {
            AppointmentResponse(
                it.id!!,
                it.patient.id!!,
                it.patient.email,
                it.slot.doctor.id!!,
                it.slot.doctor.fullName,
                it.slot.startTime,
                it.slot.endTime,
                it.status.name
            )
        }
    }
    
    @Transactional
    fun cancelAppointment(patientId: Long, appointmentId: Long): AppointmentResponse {
        val appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow { IllegalArgumentException("Appointment not found") }
        
        if (appointment.patient.id != patientId) {
            throw IllegalArgumentException("Appointment does not belong to the specified patient")
        }
        
        if (appointment.status == AppointmentStatus.CANCELLED) {
            throw IllegalArgumentException("Appointment is already cancelled")
        }
        
        if (appointment.status == AppointmentStatus.COMPLETED) {
            throw IllegalArgumentException("Cannot cancel a completed appointment")
        }
        
        appointment.status = AppointmentStatus.CANCELLED
        appointment.slot.isBooked = false
        doctorSlotRepository.save(appointment.slot)
        
        val savedAppointment = appointmentRepository.save(appointment)
        
        return AppointmentResponse(
            savedAppointment.id!!,
            savedAppointment.patient.id!!,
            savedAppointment.patient.email,
            savedAppointment.slot.doctor.id!!,
            savedAppointment.slot.doctor.fullName,
            savedAppointment.slot.startTime,
            savedAppointment.slot.endTime,
            savedAppointment.status.name
        )
    }
    
    fun getAppointmentDetails(patientId: Long, appointmentId: Long): AppointmentResponse {
        val appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow { IllegalArgumentException("Appointment not found") }
        
        if (appointment.patient.id != patientId) {
            throw IllegalArgumentException("Appointment does not belong to the specified patient")
        }
        
        return AppointmentResponse(
            appointment.id!!,
            appointment.patient.id!!,
            appointment.patient.email,
            appointment.slot.doctor.id!!,
            appointment.slot.doctor.fullName,
            appointment.slot.startTime,
            appointment.slot.endTime,
            appointment.status.name
        )
    }
} 