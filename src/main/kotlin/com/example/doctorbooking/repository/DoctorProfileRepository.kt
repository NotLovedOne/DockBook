package com.example.doctorbooking.repository

import com.example.doctorbooking.entity.DoctorProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DoctorProfileRepository : JpaRepository<DoctorProfile, Long> {
    fun findBySpecialty(specialty: String): List<DoctorProfile>
    
    @Query("SELECT DISTINCT d FROM DoctorProfile d JOIN d.slots s WHERE d.specialty = :specialty AND s.isBooked = false")
    fun findBySpecialtyAndHasAvailableSlots(@Param("specialty") specialty: String): List<DoctorProfile>
    
    @Query("SELECT DISTINCT d FROM DoctorProfile d JOIN d.slots s WHERE s.isBooked = false")
    fun findByHasAvailableSlots(): List<DoctorProfile>
    
    @Query("SELECT DISTINCT d FROM DoctorProfile d JOIN d.slots s WHERE d.specialty = :specialty AND s.startTime >= :startDate AND s.startTime < :endDate AND s.isBooked = false")
    fun findBySpecialtyAndAvailableOnDate(
        @Param("specialty") specialty: String,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<DoctorProfile>
    
    @Query("SELECT DISTINCT d FROM DoctorProfile d JOIN d.slots s WHERE s.startTime >= :startDate AND s.startTime < :endDate AND s.isBooked = false")
    fun findAvailableOnDate(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<DoctorProfile>
} 