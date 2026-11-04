package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContactScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val faqs = listOf(
        "What happens immediately after I enroll?" to "You receive instant confirmation, your order receipt, and direct access to the course in the My Learning tab. Our admissions team also contacts you on WhatsApp to add you to the exclusive live batch group and provide your 1-Year Canva Pro credentials.",
        "Are the classes live or recorded?" to "We conduct live interactive classes with experienced faculty where you can ask doubts in real-time. Additionally, high-definition class recordings are uploaded after every session so you can revise at your own pace anytime.",
        "How do I receive the 1-Year Canva Pro Subscription?" to "Canva Pro is provided to your personal email account at zero extra cost as part of the course benefits package. It remains active for 1 full year with all premium templates, fonts, and Magic Studio AI tools.",
        "How does the 100% Freelancing & Placement Guidance work?" to "We guide you through setting up an attractive Upwork & Fiverr profile, writing winning proposals, creating a portfolio of real client projects, and personal branding on LinkedIn to attract inbound job offers.",
        "Will I receive an industry-recognized certificate?" to "Yes! Upon completing the course modules and projects, you receive a verified Certificate of Completion from Harshika's Edits with a unique credential ID and verification QR code."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About & Contact Us", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("about_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // Brand Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(RoyalNavyPrimary, RoyalNavySurface)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        HarshikaLogoBadge(size = 56)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "HARSHIKA'S EDITS",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            fontSize = 22.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Empowering Individuals Through Digital Skills & AI",
                            color = GoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enhance Your Skills. Build Your Future. 🚀",
                            color = GoldBright,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Mission & Vision Card from Brochure
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = GoldMuted, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Our Mission", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "To make digital education accessible, practical, and career-oriented. We bridge the gap between traditional education and industry requirements by providing hands-on training that enables learners to develop valuable skills, build professional portfolios, and explore freelancing and remote work opportunities.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Visibility, contentDescription = null, tint = GoldMuted, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Our Vision", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "To empower individuals with the knowledge, confidence, and practical capabilities needed to explore new career paths, become financially independent through their skills, and succeed in an increasingly digital world.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            // Official Contact Channels
            item {
                SectionHeader(title = "Connect With Us", subtitle = "Direct official contact info")
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ContactChannelRow(
                            icon = Icons.Filled.Phone,
                            title = "Official Phone / Helpline",
                            value = CourseRepository.CONTACT_PHONE_FORMATTED,
                            actionLabel = "Call",
                            onClick = { launchDialer(context, CourseRepository.CONTACT_PHONE) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                        ContactChannelRow(
                            icon = Icons.Filled.Chat,
                            title = "WhatsApp Support",
                            value = "Chat with Admissions Team",
                            actionLabel = "WhatsApp",
                            onClick = {
                                launchWhatsApp(
                                    context,
                                    CourseRepository.CONTACT_WHATSAPP,
                                    "Hello Harshika's Edits team! I want information on courses and batches."
                                )
                            }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                        ContactChannelRow(
                            icon = Icons.Filled.Language,
                            title = "Official Website",
                            value = CourseRepository.WEBSITE_URL,
                            actionLabel = "Visit",
                            onClick = { launchUrl(context, CourseRepository.WEBSITE_URL) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                        ContactChannelRow(
                            icon = Icons.Filled.CameraAlt,
                            title = "Instagram Official",
                            value = CourseRepository.INSTAGRAM_HANDLE,
                            actionLabel = "Follow",
                            onClick = { launchUrl(context, CourseRepository.INSTAGRAM_URL) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                        ContactChannelRow(
                            icon = Icons.Filled.Shop,
                            title = "Google Play Store",
                            value = "com.harshikas.edits",
                            actionLabel = "Open",
                            onClick = { launchUrl(context, CourseRepository.PLAY_STORE_URL) }
                        )
                    }
                }
            }

            // FAQs Section
            item {
                Spacer(modifier = Modifier.height(18.dp))
                SectionHeader(title = "Frequently Asked Questions", subtitle = "Everything you need to know")
            }

            items(faqs) { (q, a) ->
                FaqAccordionCard(question = q, answer = a)
            }
        }
    }
}

@Composable
fun ContactChannelRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    actionLabel: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RoyalNavyPrimary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = RoyalNavyPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(value, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        FilledTonalButton(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(34.dp)
        ) {
            Text(actionLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FaqAccordionCard(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = answer,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(14.dp)
                )
            }
        }
    }
}
