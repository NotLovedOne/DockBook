package com.example.doctorbooking.service

import com.example.doctorbooking.dto.LoginRequest
import com.example.doctorbooking.dto.LoginResponse
import com.example.doctorbooking.dto.RegisterRequest
import com.example.doctorbooking.dto.UserResponse
import com.example.doctorbooking.entity.User
import com.example.doctorbooking.entity.UserRole
import com.example.doctorbooking.entity.DoctorProfile
import com.example.doctorbooking.repository.UserRepository
import com.example.doctorbooking.repository.DoctorProfileRepository
import com.example.doctorbooking.util.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val doctorProfileRepository: DoctorProfileRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val specialtyService: SpecialtyService
) {
    
    @Transactional
    fun register(request: RegisterRequest): LoginResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already registered")
        }
        
        val role = try {
            UserRole.valueOf(request.role.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid role: ${request.role}. Valid roles: PATIENT, DOCTOR, ADMIN")
        }
        
        val user = userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                role = role
            )
        )
        
        if (role == UserRole.DOCTOR) {
            doctorProfileRepository.save(
                DoctorProfile(
                    user = user,
                    fullName = request.fullName,
                    specialty = "General Practice",
                    description = "New doctor profile"
                )
            )
        }
        
        val token = jwtUtil.generateToken(user.email, user.role.name)
        return LoginResponse(token, UserResponse(user.id!!, user.email, user.role.name))
    }
    
    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Invalid email or password") }
        
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }
        
        val token = jwtUtil.generateToken(user.email, user.role.name)
        return LoginResponse(token, UserResponse(user.id!!, user.email, user.role.name))
    }
}