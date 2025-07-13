package com.example.doctorbooking.controller

import com.example.doctorbooking.service.SpecialtyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/specialties")
@Tag(name = "Specialties", description = "Medical specialties management")
class SpecialtyController(private val specialtyService: SpecialtyService) {
    
    @GetMapping
    @Operation(summary = "Get all available specialties")
    fun getSpecialties(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(specialtyService.getAllSpecialties())
    }
    
    @GetMapping("/{specialty}/doctors")
    @Operation(summary = "Get doctors by specialty")
    fun getDoctorsBySpecialty(@PathVariable specialty: String): ResponseEntity<Int> {
        return ResponseEntity.ok(specialtyService.getDoctorCountBySpecialty(specialty))
    }
    
    @GetMapping("/stats")
    @Operation(summary = "Get specialty statistics")
    fun getSpecialtyStats(): ResponseEntity<Map<String, Int>> {
        return ResponseEntity.ok(specialtyService.getSpecialtyStats())
    }
} 