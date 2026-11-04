package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CourseRepository
import com.example.model.Course
import com.example.model.CourseLesson
import com.example.model.EnrolledCourse
import com.example.ui.components.HarshikaLogoBadge
import com.example.ui.components.launchUrl
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLearningScreen(
    initialCourseId: String? = null,
    onExploreCoursesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val enrolledMap by CourseRepository.enrolledCourses.collectAsState()
    val allCourses = remember { CourseRepository.getAllCourses() }

    val enrolledList = remember(enrolledMap) {
        enrolledMap.keys.mapNotNull { id -> CourseRepository.getCourseById(id) }
    }

    var selectedCourseId by remember {
        mutableStateOf(initialCourseId ?: enrolledList.firstOrNull()?.id)
    }

    // Keep selectedCourseId valid if courses change
    LaunchedEffect(enrolledList) {
        if (selectedCourseId == null && enrolledList.isNotEmpty()) {
            selectedCourseId = enrolledList.first().id
        }
    }

    var activeLesson by remember { mutableStateOf<CourseLesson?>(null) }
    var showCertificateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Learning Hub", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "${enrolledList.size} Enrolled Courses • Live Classes & Recordings",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (enrolledList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(RoyalNavyPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.School, contentDescription = null, tint = RoyalNavyPrimary, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No Enrolled Courses Yet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Start your digital skills journey with Harshika's Edits today!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onExploreCoursesClick,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalNavyPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Explore Courses")
                    }
                }
            }
        } else {
            val currentCourse = enrolledList.find { it.id == selectedCourseId } ?: enrolledList.first()
            val enrolledData = enrolledMap[currentCourse.id] ?: EnrolledCourse(currentCourse.id, "Recent", "HE-000", "Evening")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Course Selector Chips if multiple enrolled
                if (enrolledList.size > 1) {
                    item {
                        Text("Select Course:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            enrolledList.forEach { c ->
                                val isSelected = c.id == currentCourse.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCourseId = c.id },
                                    label = { Text(c.title.take(20) + "...", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = RoyalNavyPrimary,
                                        selectedLabelColor = GoldBright
                                    )
                                )
                            }
                        }
                    }
                }

                // Active Course Progress & Live Class Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("my_course_card_${currentCourse.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RoyalNavySurface),
                        border = BorderStroke(1.dp, GoldBright.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldSuccess.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "LIVE BATCH ACTIVE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSuccess,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = enrolledData.selectedBatch.take(25) + "...",
                                    fontSize = 11.sp,
                                    color = GoldLight
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentCourse.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Progress Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Course Progress",
                                    fontSize = 12.sp,
                                    color = GoldLight.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${enrolledData.progressPercent}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GoldBright
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { enrolledData.progressPercent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = GoldBright,
                                trackColor = RoyalNavyLight
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Opening Live Interactive Class session...", Toast.LENGTH_SHORT).show()
                                        launchUrl(context, "https://meet.google.com/harshikas-edits-live")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldBright,
                                        contentColor = RoyalNavyDark
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("join_live_class_btn")
                                ) {
                                    Icon(Icons.Filled.VideoCall, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Join Live Class", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                if (enrolledData.progressPercent >= 50 || enrolledData.isCompleted) {
                                    OutlinedButton(
                                        onClick = { showCertificateDialog = true },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                                        border = BorderStroke(1.dp, GoldBright),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .testTag("view_certificate_btn")
                                    ) {
                                        Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Certificate", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Lessons & Recordings List
                item {
                    Text(
                        text = "Curriculum Lessons & Recordings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Tap on any lesson to watch class recording and complete assignments",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                currentCourse.curriculumModules.forEach { module ->
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = "Module ${module.moduleNumber}: ${module.title}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    items(module.lessons) { lesson ->
                        val isDone = enrolledData.completedLessons.contains(lesson.id)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { activeLesson = lesson }
                                .testTag("lesson_item_${lesson.id}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(
                                1.dp,
                                if (isDone) EmeraldSuccess.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        CourseRepository.toggleLessonCompletion(currentCourse.id, lesson.id)
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isDone) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                        contentDescription = "Mark done",
                                        tint = if (isDone) EmeraldSuccess else TextMuted
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = lesson.title,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Duration: ${lesson.duration} • Interactive Recording Available",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play Lesson",
                                    tint = RoyalNavyPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Interactive Lesson Player Dialog
    if (activeLesson != null) {
        val currentCourse = enrolledList.find { it.id == selectedCourseId } ?: enrolledList.first()
        val isDone = enrolledMap[currentCourse.id]?.completedLessons?.contains(activeLesson!!.id) == true

        var isPlaying by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { activeLesson = null },
            confirmButton = {
                Button(
                    onClick = {
                        CourseRepository.toggleLessonCompletion(currentCourse.id, activeLesson!!.id)
                        activeLesson = null
                        Toast.makeText(context, "Lesson progress updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text(if (isDone) "Mark as Incomplete" else "Mark as Completed ✓")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeLesson = null }) {
                    Text("Close")
                }
            },
            title = {
                Text(text = activeLesson!!.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    // Video Player Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RoyalNavyDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.PauseCircle else Icons.Filled.PlayCircle,
                                contentDescription = "Play/Pause",
                                tint = GoldBright,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clickable { isPlaying = !isPlaying }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isPlaying) "Playing Recording • 1080p HD" else "Paused",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Lesson Notes & Practical Task:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Follow along with the mentor session. Create your project file in Canva or your editor, export it in 1080p, and submit to the trainer group for review.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        )
    }

    // Certificate of Completion Dialog
    if (showCertificateDialog) {
        val currentCourse = enrolledList.find { it.id == selectedCourseId } ?: enrolledList.first()
        val enrolledData = enrolledMap[currentCourse.id]

        AlertDialog(
            onDismissRequest = { showCertificateDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Certificate downloaded to device!", Toast.LENGTH_LONG).show()
                        showCertificateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalNavyPrimary)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download PDF")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCertificateDialog = false }) {
                    Text("Close")
                }
            },
            text = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
                    border = BorderStroke(2.dp, GoldBright)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HarshikaLogoBadge(size = 40)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "CERTIFICATE OF COMPLETION",
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = RoyalNavyPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Harshika's Edits • Registered EdTech",
                            fontSize = 9.sp,
                            color = GoldMuted
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "This is proudly presented to",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "Certified Learner",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                            color = RoyalNavyDark
                        )
                        Text(
                            text = "for successfully mastering practical industry skills in",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = currentCourse.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = RoyalNavyPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = GoldBright.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("HARSHIKA", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Cursive, fontSize = 14.sp, color = RoyalNavyPrimary)
                                Text("Director & Founder", fontSize = 9.sp, color = Color(0xFF64748B))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("ID: ${enrolledData?.certificateId ?: "HE-VERIFIED-9821"}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RoyalNavyPrimary)
                                Text("Verify Online", fontSize = 8.sp, color = EmeraldSuccess)
                            }
                        }
                    }
                }
            }
        )
    }
}
