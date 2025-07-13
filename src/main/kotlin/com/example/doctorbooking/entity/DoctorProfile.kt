package com.example.doctorbooking.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "doctor_profiles")
class DoctorProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @Column(name = "full_name", nullable = false)
    var fullName: String,
    
    @Column(nullable = false)
    var specialty: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String,
    
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val slots: List<DoctorSlot> = emptyList(),
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) 