package com.example.model

enum class CourseCategory(val label: String) {
    ALL("All Courses"),
    AI_AND_DESIGN("AI & Design"),
    CAREER_DIPLOMA("Career & Diploma"),
    VIDEO_EDITING("Video Editing"),
    DIGITAL_MARKETING("Digital Marketing"),
    FREELANCING("Freelancing & SEO"),
    WEB_DEV("Web Development")
}

data class CourseLesson(
    val id: String,
    val title: String,
    val duration: String,
    val isFreePreview: Boolean = false,
    val videoUrl: String = "",
    val summary: String = ""
)

data class CourseModule(
    val moduleNumber: Int,
    val title: String,
    val description: String,
    val lessons: List<CourseLesson>
)

data class Course(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: CourseCategory,
    val duration: String,
    val mode: String = "Live Online Classes + Recordings",
    val price: Int,
    val originalPrice: Int,
    val rating: Float = 4.9f,
    val reviewCount: Int = 120,
    val enrolledCount: Int = 1500,
    val tag: String? = null,
    val overview: String,
    val whatYouWillLearn: List<String>,
    val topicsCovered: List<String>,
    val curriculumModules: List<CourseModule>,
    val specialBenefits: List<String>,
    val idealFor: List<String>,
    val earningOpportunity: String? = null,
    val iconKey: String = "diploma"
) {
    val discountPercent: Int
        get() = if (originalPrice > 0) (((originalPrice - price).toDouble() / originalPrice) * 100).toInt() else 0
}
