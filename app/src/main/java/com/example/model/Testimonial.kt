package com.example.model

data class Testimonial(
    val id: String,
    val name: String,
    val role: String,
    val location: String,
    val courseName: String,
    val rating: Float,
    val comment: String,
    val earningsOutcome: String,
    val verifiedBuyer: Boolean = true,
    val date: String = "Recent",
    val avatarInitial: String = name.take(1)
)
