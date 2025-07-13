package com.example.doctorbooking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DoctorBookingApplication

fun main(args: Array<String>) {
    runApplication<DoctorBookingApplication>(*args)
}