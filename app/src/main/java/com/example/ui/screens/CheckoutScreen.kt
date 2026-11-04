package com.example.ui.screens

import android.app.Activity
import android.widget.Toast
import com.example.ads.RewardedAdManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CourseRepository
import com.example.model.OrderReceipt
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    courseId: String,
    onBackClick: () -> Unit,
    onPurchaseSuccess: (OrderReceipt) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val course = remember(courseId) { CourseRepository.getCourseById(courseId) }

    if (course == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = onBackClick) { Text("Go Back") }
        }
        return
    }

    val batchOptions = listOf(
        "Evening Live Batch (7:00 PM - 8:30 PM) • Recommended",
        "Morning Live Batch (10:00 AM - 11:30 AM)",
        "Weekend Fast-Track (Sat & Sun 4:00 PM - 7:00 PM)"
    )

    var selectedBatch by remember { mutableStateOf(batchOptions.first()) }
    var learnerName by remember { mutableStateOf("") }
    var learnerPhone by remember { mutableStateOf("") }
    var learnerEmail by remember { mutableStateOf("") }

    var couponInput by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var couponDiscount by remember { mutableStateOf(0) }
    var couponMessage by remember { mutableStateOf<String?>(null) }

    var adRewardDiscount by remember { mutableStateOf(0) }
    var hasWatchedAd by remember { mutableStateOf(false) }

    val paymentMethods = listOf(
        "UPI (Google Pay, PhonePe, Paytm, BHIM)" to Icons.Filled.AccountBalanceWallet,
        "Debit / Credit Card (Visa, RuPay, Mastercard)" to Icons.Filled.CreditCard,
        "Net Banking (All Indian Banks)" to Icons.Filled.AccountBalance,
        "No-Cost EMI / Pay Later" to Icons.Filled.Schedule
    )
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods.first().first) }

    var isProcessing by remember { mutableStateOf(false) }

    val finalPrice = (course.price - couponDiscount - adRewardDiscount).coerceAtLeast(1)

    fun applyCouponCode(code: String) {
        val discount = CourseRepository.applyCoupon(code, course.price)
        if (discount > 0) {
            appliedCoupon = code.trim().uppercase()
            couponDiscount = discount
            couponMessage = "Coupon applied! You saved ₹$discount"
            Toast.makeText(context, "Saved ₹$discount with $appliedCoupon", Toast.LENGTH_SHORT).show()
        } else {
            appliedCoupon = null
            couponDiscount = 0
            couponMessage = "Invalid coupon code"
            Toast.makeText(context, "Invalid Coupon Code", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Your Enrollment", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("checkout_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().shadow(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Payable",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "₹$finalPrice",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (couponDiscount > 0) {
                                Text(
                                    text = "₹${course.price}",
                                    fontSize = 13.sp,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (learnerName.isBlank()) {
                                Toast.makeText(context, "Please enter learner name for certificate", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (learnerPhone.isBlank()) {
                                Toast.makeText(context, "Please enter phone number for batch link", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isProcessing = true
                            coroutineScope.launch {
                                // Simulate ultra-fast secure payment authorization
                                delay(1200)
                                val receipt = CourseRepository.enrollInCourse(
                                    course = course,
                                    selectedBatch = selectedBatch,
                                    amountPaid = finalPrice,
                                    discountApplied = (course.originalPrice - finalPrice),
                                    couponCode = appliedCoupon,
                                    paymentMethod = selectedPaymentMethod,
                                    learnerName = learnerName,
                                    learnerPhone = learnerPhone
                                )
                                isProcessing = false
                                onPurchaseSuccess(receipt)
                            }
                        },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalNavyPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("pay_securely_btn")
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = GoldBright,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Processing...", fontSize = 13.sp)
                        } else {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = GoldBright, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pay ₹$finalPrice Securely", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Selected Course Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RoyalNavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.School, contentDescription = null, tint = GoldBright, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = course.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${course.duration} • ${course.mode}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Includes 1-Year Canva Pro FREE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Batch Selection
            item {
                Text(
                    text = "Select Preferred Live Batch Timing",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    batchOptions.forEach { batch ->
                        val isSelected = batch == selectedBatch
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedBatch = batch }
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) GoldBright else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            color = if (isSelected) GoldBright.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedBatch = batch },
                                    colors = RadioButtonDefaults.colors(selectedColor = GoldBright)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = batch,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Learner Details Form
            item {
                Text(
                    text = "Learner Information (For Certificate & Class Access)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = learnerName,
                    onValueChange = { learnerName = it },
                    label = { Text("Full Name (as on Certificate)") },
                    placeholder = { Text("e.g. Rahul Sharma") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_learner_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = learnerPhone,
                    onValueChange = { learnerPhone = it },
                    label = { Text("WhatsApp Number (for Live Class Link)") },
                    placeholder = { Text("e.g. +91 9876543210") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_learner_phone")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = learnerEmail,
                    onValueChange = { learnerEmail = it },
                    label = { Text("Email Address (for Invoice & Recordings)") },
                    placeholder = { Text("e.g. rahul@example.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_learner_email")
                )

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Coupon Code Section
            item {
                Text(
                    text = "Apply Coupon Code",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponInput,
                        onValueChange = { couponInput = it },
                        placeholder = { Text("Enter Coupon (e.g. HARSHIKA10)") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("coupon_input")
                    )

                    Button(
                        onClick = { applyCouponCode(couponInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = RoyalNavyDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(52.dp).testTag("apply_coupon_btn")
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                }

                // Quick Coupon Suggestions
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("HARSHIKA10" to "10% OFF", "EARLYBIRD" to "₹200 OFF", "SKILLS500" to "₹500 OFF")) { (code, desc) ->
                        AssistChip(
                            onClick = {
                                couponInput = code
                                applyCouponCode(code)
                            },
                            label = { Text("$code ($desc)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            leadingIcon = { Icon(Icons.Filled.Discount, contentDescription = null, tint = GoldBright, modifier = Modifier.size(14.dp)) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }

                if (couponMessage != null) {
                    Text(
                        text = couponMessage!!,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (couponDiscount > 0) EmeraldSuccess else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Google AdMob Rewarded Ad Sponsor Discount Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rewarded_ad_checkout_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasWatchedAd) EmeraldSuccess.copy(alpha = 0.12f) else GoldBright.copy(alpha = 0.12f)
                    ),
                    border = BorderStroke(1.dp, if (hasWatchedAd) EmeraldSuccess else GoldBright)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (hasWatchedAd) Icons.Filled.CheckCircle else Icons.Filled.PlayCircle,
                            contentDescription = "Rewarded Test Ad",
                            tint = if (hasWatchedAd) EmeraldSuccess else GoldMuted,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (hasWatchedAd) "Ad Reward Unlocked: ₹150 OFF!" else "Watch Sponsor Ad for ₹150 OFF",
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
                                        text = "TEST AD",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalNavyDark,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (hasWatchedAd) "Reward granted via ca-app-pub-3940256099942544/5224354917" else "Watch short sponsor ad to get instant ₹150 discount on this course",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (!hasWatchedAd) {
                            Button(
                                onClick = {
                                    val activity = context as? Activity
                                    if (activity != null) {
                                        RewardedAdManager.showAd(
                                            activity = activity,
                                            rewardRewardTitle = "₹150 Course Discount",
                                            onRewardEarned = { _, _ ->
                                                hasWatchedAd = true
                                                adRewardDiscount = 150
                                                Toast.makeText(context, "🎉 You earned ₹150 extra discount!", Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    } else {
                                        hasWatchedAd = true
                                        adRewardDiscount = 150
                                        Toast.makeText(context, "🎉 You earned ₹150 extra discount!", Toast.LENGTH_LONG).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = RoyalNavyDark),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("watch_rewarded_ad_checkout_btn")
                            ) {
                                Text("Watch", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Payment Method Selector
            item {
                Text(
                    text = "Select Payment Method",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    paymentMethods.forEach { (method, icon) ->
                        val isSelected = method == selectedPaymentMethod
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedPaymentMethod = method }
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) RoyalNavyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(icon, contentDescription = null, tint = if (isSelected) RoyalNavyPrimary else TextMuted, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = method,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPaymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = RoyalNavyPrimary)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Price Breakdown Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Fee Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        BillRow("Course MRP", "₹${course.originalPrice}")
                        BillRow("Special Batch Discount", "- ₹${course.originalPrice - course.price}", isDiscount = true)
                        if (couponDiscount > 0) {
                            BillRow("Coupon Discount ($appliedCoupon)", "- ₹$couponDiscount", isDiscount = true)
                        }
                        if (adRewardDiscount > 0) {
                            BillRow("Rewarded Ad Discount", "- ₹$adRewardDiscount", isDiscount = true)
                        }
                        BillRow("1-Year Canva Pro Bonus", "FREE (Worth ₹4,999)", isFree = true)
                        BillRow("Course Completion Certificate", "FREE Included", isFree = true)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("₹$finalPrice", fontWeight = FontWeight.Black, fontSize = 18.sp, color = RoyalNavyPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Safe & Secure 256-bit Encrypted Checkout",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun BillRow(label: String, value: String, isDiscount: Boolean = false, isFree: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = when {
                isDiscount -> CoralHot
                isFree -> EmeraldSuccess
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
