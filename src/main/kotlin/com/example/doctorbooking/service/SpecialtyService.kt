package com.example.doctorbooking.service

import com.example.doctorbooking.repository.DoctorProfileRepository
import org.springframework.stereotype.Service

@Service
class SpecialtyService(private val doctorProfileRepository: DoctorProfileRepository) {
    
    fun getAllSpecialties(): List<String> {
        return listOf(
            "Cardiology",
            "Dermatology",
            "Endocrinology",
            "Gastroenterology",
            "General Practice",
            "Neurology",
            "Oncology",
            "Orthopedics",
            "Pediatrics",
            "Psychiatry",
            "Radiology",
            "Surgery",
            "Urology"
        )
    }
    
    fun getDoctorCountBySpecialty(specialty: String): Int {
        return doctorProfileRepository.findBySpecialty(specialty).size
    }
    
    fun getSpecialtyStats(): Map<String, Int> {
        val specialties = getAllSpecialties()
        return specialties.associateWith { getDoctorCountBySpecialty(it) }
    }
    
    fun isValidSpecialty(specialty: String): Boolean {
        return getAllSpecialties().contains(specialty)
    }
} 