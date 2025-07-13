package com.example.doctorbooking.service

import com.example.doctorbooking.dto.*
import com.example.doctorbooking.entity.*
import com.example.doctorbooking.repository.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DoctorService(
    private val doctorProfileRepository: DoctorProfileRepository,
    private val userRepository: UserRepository,
    private val doctorSlotRepository: DoctorSlotRepository,
    private val appointmentRepository: AppointmentRepository,
    private val passwordEncoder: PasswordEncoder,
    private val specialtyService: SpecialtyService
) {
    @Transactional
    fun createDoctor(request: CreateDoctorRequest): DoctorResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already registered")
        }
        
        if (!specialtyService.isValidSpecialty(request.specialty)) {
            throw IllegalArgumentException("Invalid specialty: ${request.specialty}")
        }
        
        val user = userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                role = UserRole.DOCTOR
            )
        )
        val profile = doctorProfileRepository.save(
            DoctorProfile(
                user = user,
                fullName = request.fullName,
                specialty = request.specialty,
                description = request.description ?: ""
            )
        )
        return DoctorResponse(profile.id!!, user.email, profile.fullName, profile.specialty, profile.description)
    }

    fun getDoctors(specialty: String?, available: Boolean?, date: LocalDate?): List<DoctorResponse> {
        val doctors = when {
            !specialty.isNullOrBlank() && available == true && date != null -> {
                doctorProfileRepository.findBySpecialtyAndAvailableOnDate(specialty, date.atStartOfDay(), date.plusDays(1).atStartOfDay())
            }
            !specialty.isNullOrBlank() && available == true -> {
                doctorProfileRepository.findBySpecialtyAndHasAvailableSlots(specialty)
            }
            !specialty.isNullOrBlank() -> {
                doctorProfileRepository.findBySpecialty(specialty)
            }
            available == true && date != null -> {
                doctorProfileRepository.findAvailableOnDate(date.atStartOfDay(), date.plusDays(1).atStartOfDay())
            }
            available == true -> {
                doctorProfileRepository.findByHasAvailableSlots()
            }
            else -> {
                doctorProfileRepository.findAll()
            }
        }
        return doctors.map {
            DoctorResponse(
                it.id!!, it.user.email, it.fullName, it.specialty, it.description
            )
        }
    }

    fun getDoctor(id: Long): DoctorResponse {
        val profile = doctorProfileRepository.findById(id).orElseThrow { IllegalArgumentException("Doctor not found") }
        return DoctorResponse(profile.id!!, profile.user.email, profile.fullName, profile.specialty, profile.description)
    }

    @Transactional
    fun updateDoctor(id: Long, request: UpdateDoctorRequest): DoctorResponse {
        val profile = doctorProfileRepository.findById(id).orElseThrow { IllegalArgumentException("Doctor not found") }
        
        request.fullName?.let { 
            if (it.isNotBlank()) profile.fullName = it 
        }
        
        request.specialty?.let { 
            if (it.isNotBlank()) {
                if (!specialtyService.isValidSpecialty(it)) {
                    throw IllegalArgumentException("Invalid specialty: $it")
                }
                profile.specialty = it 
            }
        }
        
        request.description?.let { 
            if (it.isNotBlank()) profile.description = it 
        }
        
        val savedProfile = doctorProfileRepository.save(profile)
        return DoctorResponse(savedProfile.id!!, savedProfile.user.email, savedProfile.fullName, savedProfile.specialty, savedProfile.description)
    }

    @Transactional
    fun createSlot(doctorId: Long, request: CreateSlotRequest): SlotResponse {
        val doctor = doctorProfileRepository.findById(doctorId).orElseThrow { IllegalArgumentException("Doctor not found") }
        
        if (request.startTime >= request.endTime) {
            throw IllegalArgumentException("Start time must be before end time")
        }
        
        if (request.startTime.isBefore(LocalDateTime.now())) {
            throw IllegalArgumentException("Cannot create slots in the past")
        }
        
        val slot = doctorSlotRepository.save(
            DoctorSlot(
                doctor = doctor,
                startTime = request.startTime,
                endTime = request.endTime,
                isBooked = false
            )
        )
        return SlotResponse(slot.id!!, slot.startTime, slot.endTime, slot.isBooked)
    }

    fun getSchedule(doctorId: Long, date: LocalDate): List<SlotResponse> {
        val start = date.atStartOfDay()
        val end = start.plusDays(1)
        val slots = doctorSlotRepository.findAvailableSlotsByDoctorAndDateRange(doctorId, start, end)
        return slots.map { SlotResponse(it.id!!, it.startTime, it.endTime, it.isBooked) }
    }

    fun getDoctorSlots(doctorId: Long): List<SlotResponse> {
        val slots = doctorSlotRepository.findByDoctorId(doctorId)
        return slots.map { SlotResponse(it.id!!, it.startTime, it.endTime, it.isBooked) }
    }

    fun getDoctorAppointments(doctorId: Long, status: String?, date: LocalDate?): List<AppointmentResponse> {
        val appointments = when {
            status != null && date != null -> {
                appointmentRepository.findByDoctorIdAndStatusAndDate(doctorId, AppointmentStatus.valueOf(status.uppercase()), date.atStartOfDay(), date.plusDays(1).atStartOfDay())
            }
            status != null -> {
                appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.valueOf(status.uppercase()))
            }
            date != null -> {
                appointmentRepository.findByDoctorIdAndDate(doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay())
            }
            else -> {
                appointmentRepository.findByDoctorId(doctorId)
            }
        }
        return appointments.map {
            AppointmentResponse(
                it.id!!,
                it.patient.id!!,
                it.patient.email,
                doctorId,
                it.slot.doctor.fullName,
                it.slot.startTime,
                it.slot.endTime,
                it.status.name
            )
        }
    }

    @Transactional
    fun updateAppointmentStatus(appointmentId: Long, status: String): AppointmentResponse {
        val appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow { IllegalArgumentException("Appointment not found") }
        
        val newStatus = try {
            AppointmentStatus.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid appointment status: $status")
        }
        
        appointment.status = newStatus
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

    @Transactional
    fun deleteSlot(doctorId: Long, slotId: Long) {
        val slot = doctorSlotRepository.findById(slotId)
            .orElseThrow { IllegalArgumentException("Slot not found") }
        
        if (slot.doctor.id != doctorId) {
            throw IllegalArgumentException("Slot does not belong to the specified doctor")
        }
        
        if (slot.isBooked) {
            throw IllegalArgumentException("Cannot delete a booked slot")
        }
        
        doctorSlotRepository.delete(slot)
    }
} 