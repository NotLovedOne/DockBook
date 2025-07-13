package com.example.doctorbooking.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "appointments")
class Appointment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: User,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    val slot: DoctorSlot,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class AppointmentStatus {
    SCHEDULED, CANCELLED, COMPLETED, NO_SHOW
} 