package com.example.ui.screens

import android.app.Activity
import android.widget.Toast
import com.example.ads.RewardedAdManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.model.CourseCategory
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    onCourseClick: (String) -> Unit,
    onEnrollClick: (String) -> Unit,
    onViewAllCoursesClick: () -> Unit,
    onViewAllTestimonialsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val enrolledCourses by CourseRepository.enrolledCourses.collectAsState()
    val testimonials by CourseRepository.testimonials.collectAsState()
    val allCourses = remember { CourseRepository.getAllCourses() }

    var isRewardUnlocked by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(CourseCategory.ALL) }

    val filteredCourses = remember(selectedCategory) {
        if (selectedCategory == CourseCategory.ALL) {
            allCourses
        } else {
            allCourses.filter { it.category == selectedCategory }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top App Bar / Royal Brand Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(RoyalNavyPrimary, RoyalNavySurface)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HarshikaLogoBadge(size = 46)
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "HARSHIKA'S EDITS",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily.Serif,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Filled.Verified,
                                    contentDescription = "Verified Institute",
                                    tint = GoldBright,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Learn Digital Skills. Build Your Career. Earn Online.",
                                color = GoldLight.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Helpline action button
                    IconButton(
                        onClick = { launchDialer(context, CourseRepository.CONTACT_PHONE) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(RoyalNavyCard)
                            .border(1.dp, GoldBright.copy(alpha = 0.5f), CircleShape)
                            .testTag("home_call_support_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "Call Support",
                            tint = GoldBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Hero Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(6.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = RoyalNavySurface),
                border = BorderStroke(1.5.dp, GoldBright.copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(RoyalNavyLight, RoyalNavyDark)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GoldBright,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Text(
                                text = "🔥 NEW BATCH STARTING MONDAY",
                                color = RoyalNavyDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "Empowering Individuals Through Digital Skills & AI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Practical training in Canva, AI Tools, Video Editing & Web Dev with 1 Year Canva Pro FREE!",
                            color = GoldLight.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onViewAllCoursesClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldBright,
                                    contentColor = RoyalNavyDark
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("hero_explore_courses_btn")
                            ) {
                                Text("Explore Courses", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    launchWhatsApp(
                                        context,
                                        CourseRepository.CONTACT_WHATSAPP,
                                        "Hi Harshika's Edits, I want to join the upcoming live batch!"
                                    )
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = GoldLight
                                ),
                                border = BorderStroke(1.dp, GoldBright),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("hero_whatsapp_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Chat on WA", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Rewarded Test Ad Sponsor Perk Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("home_rewarded_ad_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRewardUnlocked) EmeraldSuccess.copy(alpha = 0.12f) else GoldBright.copy(alpha = 0.12f)
                ),
                border = BorderStroke(1.dp, if (isRewardUnlocked) EmeraldSuccess else GoldBright)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isRewardUnlocked) Icons.Filled.CardGiftcard else Icons.Filled.PlayCircle,
                        contentDescription = "Rewarded Ad",
                        tint = if (isRewardUnlocked) EmeraldSuccess else GoldMuted,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isRewardUnlocked) "Reward Unlocked: AI Prompts + ₹150 OFF!" else "Daily Reward: Free AI Swipe File & Discount",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = GoldBright.copy(alpha = 0.35f)
                            ) {
                                Text(
                                    text = "REWARDED AD",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalNavyDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (isRewardUnlocked) "Reward Active! Use code EARLYBIRD or claim ₹150 discount at checkout." else "Watch a short sponsor ad (ca-app-pub-3940256099942544/5224354917) to unlock rewards.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (!isRewardUnlocked) {
                        Button(
                            onClick = {
                                val activity = context as? Activity
                                if (activity != null) {
                                    RewardedAdManager.showAd(
                                        activity = activity,
                                        rewardRewardTitle = "AI Swipe File + ₹150 Voucher",
                                        onRewardEarned = { _, _ ->
                                            isRewardUnlocked = true
                                            Toast.makeText(context, "🎉 Reward Unlocked! AI Prompts + ₹150 Voucher granted!", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                } else {
                                    isRewardUnlocked = true
                                    Toast.makeText(context, "🎉 Reward Unlocked! AI Prompts + ₹150 Voucher granted!", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = RoyalNavyDark),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("watch_home_rewarded_ad_btn")
                        ) {
                            Text("Watch", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Brochure Metrics & Impact Row
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MetricColumn("10K+", "Happy Learners", Icons.Filled.People)
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(32.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    )
                    MetricColumn("50+", "Expert Trainers", Icons.Filled.School)
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(32.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    )
                    MetricColumn("4.9★", "Global Rating", Icons.Filled.Star)
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(32.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    )
                    MetricColumn("100%", "Practical Learning", Icons.Filled.AutoGraph)
                }
            }
        }

        // Quick Category Filter Chips
        item {
            Spacer(modifier = Modifier.height(14.dp))
            SectionHeader(
                title = "Popular Categories",
                subtitle = "Pick a skill track to start learning"
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CourseCategory.values()) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RoyalNavyPrimary,
                            selectedLabelColor = GoldBright,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) GoldBright else MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.testTag("filter_chip_${category.name}")
                    )
                }
            }
        }

        // Courses List Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            SectionHeader(
                title = if (selectedCategory == CourseCategory.ALL) "Trending Courses" else selectedCategory.label,
                subtitle = "Instructor-led live training with certifications",
                actionText = "See All (${allCourses.size})",
                onActionClick = onViewAllCoursesClick
            )
        }

        items(filteredCourses.take(4)) { course ->
            CourseCard(
                course = course,
                onCourseClick = onCourseClick,
                onEnrollClick = onEnrollClick,
                isEnrolled = enrolledCourses.containsKey(course.id)
            )
        }

        // "Why Choose Harshika's Edits" Section from Brochure page 2 & 13
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Why Choose Harshika's Edits?",
                subtitle = "Bridge the gap between learning and earning"
            )

            WhyChooseUsCard(
                title = "Live Interactive Classes",
                description = "Attend instructor-led online sessions and interact directly with expert faculty.",
                icon = Icons.Filled.LiveTv
            )
            WhyChooseUsCard(
                title = "Recorded Sessions Available",
                description = "Access class recordings anytime to revise lessons at your convenience.",
                icon = Icons.Filled.VideoLibrary
            )
            WhyChooseUsCard(
                title = "Practical & Project-Based Learning",
                description = "Apply concepts through hands-on activities, real-world assignments & projects.",
                icon = Icons.Filled.BuildCircle
            )
            WhyChooseUsCard(
                title = "1-Year Canva Pro FREE Bonus",
                description = "Get commercial tools free so you can start creating and freelancing immediately.",
                icon = Icons.Filled.CardGiftcard
            )
            WhyChooseUsCard(
                title = "100% Freelancing & Job Assistance",
                description = "Mentorship on Upwork, Fiverr, LinkedIn personal branding, and client acquisition.",
                icon = Icons.Filled.Work
            )
        }

        // Student Testimonials Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Student Success & Feedback",
                subtitle = "Real results from our 10,000+ happy learners",
                actionText = "View All (${testimonials.size})",
                onActionClick = onViewAllTestimonialsClick
            )
        }

        items(testimonials.take(3)) { testimonial ->
            TestimonialCard(testimonial = testimonial)
        }

        // Contact & Help Hub
        item {
            ContactHelpBanner()
        }
    }
}

@Composable
private fun MetricColumn(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GoldMuted,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
