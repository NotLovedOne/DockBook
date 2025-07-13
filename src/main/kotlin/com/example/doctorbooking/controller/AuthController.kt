package com.example.doctorbooking.controller

import com.example.doctorbooking.dto.LoginRequest
import com.example.doctorbooking.dto.LoginResponse
import com.example.doctorbooking.dto.RegisterRequest
import com.example.doctorbooking.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
class AuthController(private val authService: AuthService) {
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authService.register(request))
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authService.login(request))
    }
} 