package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CourseRepository
import com.example.model.Testimonial
import com.example.ui.components.TestimonialCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestimonialsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val testimonials by CourseRepository.testimonials.collectAsState()
    val allCourses = remember { CourseRepository.getAllCourses() }

    var selectedFilterCourse by remember { mutableStateOf("All") }
    var showWriteReviewDialog by remember { mutableStateOf(false) }

    val courseFilterOptions = remember(allCourses) {
        listOf("All") + allCourses.map { it.title.take(22) + "..." }
    }

    val filteredList = remember(testimonials, selectedFilterCourse) {
        if (selectedFilterCourse == "All") {
            testimonials
        } else {
            val key = selectedFilterCourse.replace("...", "").trim().lowercase()
            testimonials.filter { it.courseName.lowercase().contains(key) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Student Feedback & Reviews", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Real stories & earnings from our learners", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("testimonials_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { showWriteReviewDialog = true },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = GoldBright.copy(alpha = 0.2f),
                            contentColor = RoyalNavyDark
                        ),
                        modifier = Modifier.padding(end = 8.dp).testTag("write_review_btn")
                    ) {
                        Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Write Review", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Overall Rating Summary Header
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "4.9",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row {
                                        repeat(5) {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = null,
                                                tint = GoldBright,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Based on 1,200+ reviews",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "98% of students reported landing freelance gigs or career upgrades",
                                fontSize = 12.sp,
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Text(
                    text = "Filter by Course:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(courseFilterOptions) { opt ->
                        val isSelected = opt == selectedFilterCourse
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilterCourse = opt },
                            label = { Text(opt, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalNavyPrimary,
                                selectedLabelColor = GoldBright
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Reviews List
            items(filteredList) { t ->
                TestimonialCard(testimonial = t)
            }
        }
    }

    // Write Review Dialog
    if (showWriteReviewDialog) {
        var nameInput by remember { mutableStateOf("") }
        var locationInput by remember { mutableStateOf("") }
        var roleInput by remember { mutableStateOf("") }
        var courseSelected by remember { mutableStateOf(allCourses.first().title) }
        var ratingVal by remember { mutableStateOf(5) }
        var feedbackInput by remember { mutableStateOf("") }
        var earningsInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showWriteReviewDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (nameInput.isBlank() || feedbackInput.isBlank()) {
                            Toast.makeText(context, "Please enter your name and review", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        CourseRepository.addTestimonial(
                            name = nameInput,
                            role = roleInput.ifBlank { "Learner" },
                            location = locationInput.ifBlank { "India" },
                            courseName = courseSelected,
                            rating = ratingVal.toFloat(),
                            comment = feedbackInput,
                            earningsOutcome = earningsInput.ifBlank { "Learned In-Demand Skills" }
                        )
                        showWriteReviewDialog = false
                        Toast.makeText(context, "Thank you for your valuable feedback!", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalNavyPrimary)
                ) {
                    Text("Submit Review")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWriteReviewDialog = false }) {
                    Text("Cancel")
                }
            },
            title = {
                Text("Share Your Experience", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            label = { Text("City") },
                            placeholder = { Text("e.g. Pune") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = roleInput,
                            onValueChange = { roleInput = it },
                            label = { Text("Role") },
                            placeholder = { Text("Freelancer / Student") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Rating Star Picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Your Rating:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Row {
                            (1..5).forEach { star ->
                                Icon(
                                    imageVector = if (star <= ratingVal) Icons.Filled.Star else Icons.Filled.StarOutline,
                                    contentDescription = "$star Stars",
                                    tint = GoldBright,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable { ratingVal = star }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = earningsInput,
                        onValueChange = { earningsInput = it },
                        label = { Text("Career Outcome / Earning Milestone") },
                        placeholder = { Text("e.g. Earns ₹20,000/mo on Upwork") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = feedbackInput,
                        onValueChange = { feedbackInput = it },
                        label = { Text("Your Detailed Review") },
                        placeholder = { Text("How was the live trainer, syllabus, Canva Pro, etc.?") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
