package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.ads.RewardedAdManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderReceipt
import com.example.ui.screens.*
import com.example.ui.theme.GoldBright
import com.example.ui.theme.HarshikasEditsTheme
import com.example.ui.theme.RoyalNavyPrimary

sealed class Screen {
    object Home : Screen()
    object Courses : Screen()
    object MyLearning : Screen()
    object Testimonials : Screen()
    object About : Screen()
    data class CourseDetail(val courseId: String) : Screen()
    data class Checkout(val courseId: String) : Screen()
    data class OrderSuccess(val receipt: OrderReceipt) : Screen()
}

enum class BottomTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Filled.Home),
    COURSES("Courses", Icons.Filled.School),
    MY_LEARNING("My Learning", Icons.Filled.VideoLibrary),
    REVIEWS("Reviews", Icons.Filled.Star),
    ABOUT("About", Icons.Filled.Info)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RewardedAdManager.initialize(this)
        enableEdgeToEdge()
        setContent {
            HarshikasEditsTheme {
                HarshikasEditsApp()
            }
        }
    }
}

@Composable
fun HarshikasEditsApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }
    var initialCourseForLearning by remember { mutableStateOf<String?>(null) }

    // Synchronize tab when root screens change
    fun navigateToTab(tab: BottomTab) {
        selectedTab = tab
        currentScreen = when (tab) {
            BottomTab.HOME -> Screen.Home
            BottomTab.COURSES -> Screen.Courses
            BottomTab.MY_LEARNING -> Screen.MyLearning
            BottomTab.REVIEWS -> Screen.Testimonials
            BottomTab.ABOUT -> Screen.About
        }
    }

    // Handle system back navigation
    BackHandler(enabled = currentScreen !is Screen.Home) {
        when (currentScreen) {
            is Screen.CourseDetail -> {
                currentScreen = when (selectedTab) {
                    BottomTab.COURSES -> Screen.Courses
                    else -> Screen.Home
                }
            }
            is Screen.Checkout -> {
                val cId = (currentScreen as Screen.Checkout).courseId
                currentScreen = Screen.CourseDetail(cId)
            }
            is Screen.OrderSuccess -> {
                navigateToTab(BottomTab.MY_LEARNING)
            }
            else -> {
                navigateToTab(BottomTab.HOME)
            }
        }
    }

    val isRootTab = currentScreen is Screen.Home ||
            currentScreen is Screen.Courses ||
            currentScreen is Screen.MyLearning ||
            currentScreen is Screen.Testimonials ||
            currentScreen is Screen.About

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isRootTab) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    BottomTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { navigateToTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = RoyalNavyPrimary,
                                selectedTextColor = RoyalNavyPrimary,
                                indicatorColor = GoldBright.copy(alpha = 0.35f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    is Screen.Home -> {
                        HomeScreen(
                            onCourseClick = { courseId ->
                                currentScreen = Screen.CourseDetail(courseId)
                            },
                            onEnrollClick = { courseId ->
                                currentScreen = Screen.Checkout(courseId)
                            },
                            onViewAllCoursesClick = {
                                navigateToTab(BottomTab.COURSES)
                            },
                            onViewAllTestimonialsClick = {
                                navigateToTab(BottomTab.REVIEWS)
                            }
                        )
                    }
                    is Screen.Courses -> {
                        CoursesListScreen(
                            onCourseClick = { courseId ->
                                currentScreen = Screen.CourseDetail(courseId)
                            },
                            onEnrollClick = { courseId ->
                                currentScreen = Screen.Checkout(courseId)
                            }
                        )
                    }
                    is Screen.CourseDetail -> {
                        CourseDetailScreen(
                            courseId = screen.courseId,
                            onBackClick = {
                                currentScreen = when (selectedTab) {
                                    BottomTab.COURSES -> Screen.Courses
                                    else -> Screen.Home
                                }
                            },
                            onEnrollClick = { courseId ->
                                currentScreen = Screen.Checkout(courseId)
                            },
                            onOpenCourseLessons = { courseId ->
                                initialCourseForLearning = courseId
                                navigateToTab(BottomTab.MY_LEARNING)
                            }
                        )
                    }
                    is Screen.Checkout -> {
                        CheckoutScreen(
                            courseId = screen.courseId,
                            onBackClick = {
                                currentScreen = Screen.CourseDetail(screen.courseId)
                            },
                            onPurchaseSuccess = { receipt ->
                                currentScreen = Screen.OrderSuccess(receipt)
                            }
                        )
                    }
                    is Screen.OrderSuccess -> {
                        OrderSuccessScreen(
                            receipt = screen.receipt,
                            onStartLearningClick = { courseId ->
                                initialCourseForLearning = courseId
                                navigateToTab(BottomTab.MY_LEARNING)
                            },
                            onGoHomeClick = {
                                navigateToTab(BottomTab.HOME)
                            }
                        )
                    }
                    is Screen.MyLearning -> {
                        MyLearningScreen(
                            initialCourseId = initialCourseForLearning,
                            onExploreCoursesClick = {
                                navigateToTab(BottomTab.COURSES)
                            }
                        )
                    }
                    is Screen.Testimonials -> {
                        TestimonialsScreen(
                            onBackClick = {
                                navigateToTab(BottomTab.HOME)
                            }
                        )
                    }
                    is Screen.About -> {
                        AboutContactScreen(
                            onBackClick = {
                                navigateToTab(BottomTab.HOME)
                            }
                        )
                    }
                }
            }
        }
    }
}
