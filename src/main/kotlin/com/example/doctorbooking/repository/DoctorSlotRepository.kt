package com.example.doctorbooking.repository

import com.example.doctorbooking.entity.DoctorSlot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DoctorSlotRepository : JpaRepository<DoctorSlot, Long> {
    fun findByDoctorId(doctorId: Long): List<DoctorSlot>
    
    @Query("SELECT s FROM DoctorSlot s WHERE s.doctor.id = :doctorId AND s.startTime >= :startDate AND s.startTime < :endDate AND s.isBooked = false")
    fun findAvailableSlotsByDoctorAndDateRange(
        @Param("doctorId") doctorId: Long,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<DoctorSlot>
} 