package com.example.doctorbooking.repository

import com.example.doctorbooking.entity.Appointment
import com.example.doctorbooking.entity.AppointmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {
    fun findByPatientId(patientId: Long): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.slot.doctor.id = :doctorId")
    fun findByDoctorId(@Param("doctorId") doctorId: Long): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.slot.doctor.id = :doctorId AND a.status = :status")
    fun findByDoctorIdAndStatus(@Param("doctorId") doctorId: Long, @Param("status") status: AppointmentStatus): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.slot.doctor.id = :doctorId AND a.slot.startTime >= :startDate AND a.slot.startTime < :endDate")
    fun findByDoctorIdAndDate(
        @Param("doctorId") doctorId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.slot.doctor.id = :doctorId AND a.status = :status AND a.slot.startTime >= :startDate AND a.slot.startTime < :endDate")
    fun findByDoctorIdAndStatusAndDate(
        @Param("doctorId") doctorId: Long,
        @Param("status") status: AppointmentStatus,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
    fun findByPatientIdAndStatus(@Param("patientId") patientId: Long, @Param("status") status: AppointmentStatus): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.slot.startTime >= :startDate AND a.slot.startTime < :endDate")
    fun findByPatientIdAndDate(
        @Param("patientId") patientId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Appointment>
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status AND a.slot.startTime >= :startDate AND a.slot.startTime < :endDate")
    fun findByPatientIdAndStatusAndDate(
        @Param("patientId") patientId: Long,
        @Param("status") status: AppointmentStatus,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Appointment>
} 