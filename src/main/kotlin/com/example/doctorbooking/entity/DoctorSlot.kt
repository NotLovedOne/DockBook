package com.example.doctorbooking.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "doctor_slots")
class DoctorSlot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    val doctor: DoctorProfile,
    
    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,
    
    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,
    
    @Column(name = "is_booked", nullable = false)
    var isBooked: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) 