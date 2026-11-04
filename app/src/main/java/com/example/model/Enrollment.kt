package com.example.model

data class EnrolledCourse(
    val courseId: String,
    val enrollmentDate: String,
    val orderId: String,
    val selectedBatch: String,
    val progressPercent: Int = 15,
    val completedLessons: Set<String> = emptySet(),
    val isCompleted: Boolean = false,
    val certificateId: String? = null
)

data class CertificateData(
    val certificateId: String,
    val studentName: String,
    val courseName: String,
    val issueDate: String,
    val verificationCode: String,
    val directorName: String = "Harshika",
    val instituteName: String = "Harshika's Edits EdTech"
)

data class OrderReceipt(
    val orderId: String,
    val courseId: String,
    val courseTitle: String,
    val selectedBatch: String,
    val amountPaid: Int,
    val originalPrice: Int,
    val discountApplied: Int,
    val couponCode: String?,
    val paymentMethod: String,
    val transactionId: String,
    val timestamp: String,
    val learnerName: String,
    val learnerPhone: String
)
