package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CustomerRole
import com.example.model.OtpStep
import com.example.model.UserAccount
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.StoneBrownDark
import com.example.ui.theme.StoneBrownPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenTab

@Composable
fun AuthScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val language by viewModel.language.collectAsState()
    val otpStep by viewModel.otpStep.collectAsState()
    val otpError by viewModel.otpError.collectAsState()
    val resendCountdown by viewModel.resendCountdown.collectAsState()
    val quoteItems by viewModel.quoteItems.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    // Form inputs for login
    var phoneInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var cityInput by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(CustomerRole.HOME_BUILDER) }
    var otpCodeInput by remember { mutableStateOf("") }

    // Pre-fill inputs from existing user if logged in
    var isEditingProfile by remember { mutableStateOf(false) }
    var editName by remember(currentUser) { mutableStateOf(currentUser.fullName) }
    var editCity by remember(currentUser) { mutableStateOf(currentUser.deliveryCity) }
    var editGstin by remember(currentUser) { mutableStateOf(currentUser.gstin) }
    var editRole by remember(currentUser) { mutableStateOf(currentUser.role) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("auth_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (currentUser.isLoggedIn) {
            // ==========================================
            // LOGGED IN DASHBOARD VIEW
            // ==========================================

            // Header profile badge
            Surface(
                color = GoldContainer,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GoldAccent),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = StoneBrownDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.EN) "OTP VERIFIED ACCOUNT" else "OTP सत्यापित ग्राहक खाता",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StoneBrownDark
                    )
                }
            }

            // User Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar Circle with initial
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(StoneBrownPrimary)
                            .border(3.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val initial = currentUser.fullName.trim().take(1).uppercase().ifEmpty { "C" }
                        Text(
                            text = initial,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentUser.fullName.ifBlank { "Registered Buyer" },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = WhatsAppGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+91 ${currentUser.phoneNumber}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.EN) currentUser.role.titleEn else currentUser.role.titleHi,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = StoneBrownDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (currentUser.deliveryCity.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Site: ${currentUser.deliveryCity}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (currentUser.gstin.isNotBlank()) {
                        Text(
                            text = "GSTIN: ${currentUser.gstin}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Overview Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Cart Items Shortcut Card
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.selectTab(ScreenTab.QUOTE) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = StoneBrownPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${quoteItems.size}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.EN) "Saved Items" else "कोटेशन आइटम्स",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 50-50 Payment Milestone Card
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.selectTab(ScreenTab.QUOTE) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Payment",
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "50% - 50%",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.EN) "Advance Terms" else "अग्रिम भुगतान",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Collapsible Section
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isEditingProfile = !isEditingProfile },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = StoneBrownPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.EN) "Edit Profile & GST Details" else "प्रोफ़ाइल व GST विवरण बदलें",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = if (isEditingProfile) "Close" else "Edit",
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    if (isEditingProfile) {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text(if (language == AppLanguage.EN) "Full Name" else "पूरा नाम") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editCity,
                            onValueChange = { editCity = it },
                            label = { Text(if (language == AppLanguage.EN) "Delivery City / State" else "डिलीवरी शहर / राज्य") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = editGstin,
                            onValueChange = { editGstin = it.uppercase() },
                            label = { Text(if (language == AppLanguage.EN) "Business GSTIN (Optional)" else "जीएसटीआईएन (वैकल्पिक)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.updateProfile(editName, editRole, editCity, editGstin)
                                isEditingProfile = false
                                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (language == AppLanguage.EN) "Save Profile Changes" else "बदलाव सेव करें")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Action Button
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
                border = BorderStroke(1.dp, Color(0xFFC62828)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("logout_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.EN) "Logout from Account" else "अकाउंट से लॉगआउट करें",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Shri Parshvnath Trading Co. • Ramganjmandi (Kota)",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        } else {
            // ==========================================
            // LOGIN & OTP FLOW
            // ==========================================

            // Company Title Banner
            Surface(
                color = GoldContainer,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GoldAccent),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "卐 SHRI PARSHAVNATH TRADING CO. 卐",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = StoneBrownDark,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            // Shield Icon
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Security",
                        tint = GoldAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (val step = otpStep) {
                is OtpStep.EnterPhone -> {
                    // ==============================
                    // STEP 1: ENTER PHONE NUMBER
                    // ==============================
                    Text(
                        text = if (language == AppLanguage.EN) "Customer Login" else "ग्राहक लॉगिन",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (language == AppLanguage.EN)
                            "Enter your 10-digit mobile number to receive an instant verification OTP code."
                        else
                            "तुरंत सत्यापन OTP प्राप्त करने के लिए अपना 10 अंकों का मोबाइल नंबर दर्ज करें।",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {

                            // Mobile number field
                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = { raw ->
                                    val digits = raw.filter { it.isDigit() }.take(10)
                                    phoneInput = digits
                                },
                                label = { Text(if (language == AppLanguage.EN) "Mobile Number" else "मोबाइल नंबर") },
                                leadingIcon = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                                    ) {
                                        Text(
                                            text = "🇮🇳 +91",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .height(20.dp)
                                                .width(1.dp)
                                                .background(MaterialTheme.colorScheme.outline)
                                        )
                                    }
                                },
                                placeholder = { Text("9876543210") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_phone_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Name field
                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = { nameInput = it },
                                label = { Text(if (language == AppLanguage.EN) "Your Name / Firm" else "आपका नाम / फर्म") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = StoneBrownPrimary)
                                },
                                placeholder = { Text("e.g. Ramesh Kumar / Sharma Builders") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_name_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // City field
                            OutlinedTextField(
                                value = cityInput,
                                onValueChange = { cityInput = it },
                                label = { Text(if (language == AppLanguage.EN) "Delivery City / State" else "डिलीवरी शहर / राज्य") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = StoneBrownPrimary)
                                },
                                placeholder = { Text("e.g. Ahmedabad / Jaipur / Delhi") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_city_input")
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Customer Role selection chips
                            Text(
                                text = if (language == AppLanguage.EN) "I am a:" else "मेरी श्रेणी:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CustomerRole.values().take(2).forEach { role ->
                                    val isSelected = selectedRole == role
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedRole = role },
                                        label = {
                                            Text(
                                                text = if (language == AppLanguage.EN) role.titleEn.split("/").first() else role.titleHi.split("/").first(),
                                                fontSize = 11.sp
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = GoldContainer,
                                            selectedLabelColor = StoneBrownDark
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CustomerRole.values().drop(2).forEach { role ->
                                    val isSelected = selectedRole == role
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedRole = role },
                                        label = {
                                            Text(
                                                text = if (language == AppLanguage.EN) role.titleEn.split("/").first() else role.titleHi.split("/").first(),
                                                fontSize = 11.sp
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = GoldContainer,
                                            selectedLabelColor = StoneBrownDark
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            if (otpError != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = otpError!!,
                                    color = Color(0xFFD32F2F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Send OTP Button
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    viewModel.requestOtp(phoneInput, nameInput, selectedRole, cityInput)
                                },
                                enabled = phoneInput.length == 10,
                                colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("send_otp_button")
                            ) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.EN) "Send OTP Verification Code" else "OTP सत्यापन कोड भेजें",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Demo / Instant Login Tip Card
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = WhatsAppGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.EN)
                                    "Quick Test: Enter any 10-digit number (e.g. 9876543210). The demo OTP code will be shown instantly on the next screen."
                                else
                                    "त्वरित परीक्षण: कोई भी 10 अंकों का नंबर डालें। अगली स्क्रीन पर डेमो OTP तुरंत दिखाई देगा।",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                is OtpStep.EnterOtp -> {
                    // ==============================
                    // STEP 2: VERIFY OTP
                    // ==============================
                    Text(
                        text = if (language == AppLanguage.EN) "Verify Mobile OTP" else "मोबाइल OTP सत्यापित करें",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (language == AppLanguage.EN)
                            "We sent a 6-digit verification code to +91 ${step.phone}"
                        else
                            "हमने +91 ${step.phone} पर 6 अंकों का सत्यापन कोड भेजा है।",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sandbox Helper Banner for instant auto-fill!
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GoldContainer),
                        border = BorderStroke(1.dp, GoldAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = StoneBrownDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.EN) "DEMO OTP CODE: ${step.generatedOtp}" else "डेमो OTP कोड: ${step.generatedOtp}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = StoneBrownDark
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = {
                                    otpCodeInput = step.generatedOtp
                                    focusManager.clearFocus()
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, StoneBrownDark),
                                modifier = Modifier.testTag("autofill_otp_button")
                            ) {
                                Text(
                                    text = if (language == AppLanguage.EN) "Auto-Fill Code (${step.generatedOtp})" else "कोड स्वतः भरें (${step.generatedOtp})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StoneBrownDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            OutlinedTextField(
                                value = otpCodeInput,
                                onValueChange = { raw ->
                                    val digits = raw.filter { it.isDigit() }.take(6)
                                    otpCodeInput = digits
                                },
                                label = { Text(if (language == AppLanguage.EN) "Enter 6-Digit OTP" else "6 अंकों का OTP दर्ज करें") },
                                placeholder = { Text("• • • • • •") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        if (otpCodeInput.length == 6) {
                                            viewModel.verifyAndLogin(
                                                phone = step.phone,
                                                enteredOtp = otpCodeInput,
                                                fullName = nameInput,
                                                role = selectedRole,
                                                city = cityInput
                                            )
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("otp_code_input")
                            )

                            if (otpError != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = otpError!!,
                                    color = Color(0xFFD32F2F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Verify & Login Button
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    val success = viewModel.verifyAndLogin(
                                        phone = step.phone,
                                        enteredOtp = otpCodeInput,
                                        fullName = nameInput,
                                        role = selectedRole,
                                        city = cityInput
                                    )
                                    if (success) {
                                        Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                enabled = otpCodeInput.length == 6,
                                colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("verify_otp_button")
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.EN) "Verify OTP & Login" else "OTP सत्यापित कर लॉगिन करें",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Resend OTP Countdown row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (resendCountdown > 0) {
                                    Text(
                                        text = if (language == AppLanguage.EN) "Resend OTP in ${resendCountdown}s" else "पुनः OTP भेजें (${resendCountdown}s)",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    TextButton(
                                        onClick = {
                                            viewModel.resendOtp(step.phone)
                                            Toast.makeText(context, "New OTP sent!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.testTag("resend_otp_button")
                                    ) {
                                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.EN) "Resend OTP Code" else "नया OTP कोड भेजें",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = GoldAccent
                                        )
                                    }
                                }
                            }

                            // Edit phone number
                            TextButton(
                                onClick = { viewModel.startOtpFlow() }
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.EN) "Change Mobile Number" else "मोबाइल नंबर बदलें",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                is OtpStep.Verified -> {
                    // Verified transient state will immediately transition to the logged-in dashboard view
                }
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color(0xFFC62828)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.EN) "Confirm Logout" else "लॉगआउट की पुष्टि",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    text = if (language == AppLanguage.EN)
                        "Are you sure you want to log out? Your quote cart and calculator estimates will remain safely stored on this device."
                    else
                        "क्या आप वाकई लॉगआउट करना चाहते हैं? आपकी कोटेशन सूची और कैलकुलेटर अनुमान इस डिवाइस पर सुरक्षित रहेंगे।",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    modifier = Modifier.testTag("confirm_logout_button")
                ) {
                    Text(if (language == AppLanguage.EN) "Yes, Logout" else "हाँ, लॉगआउट करें", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text(if (language == AppLanguage.EN) "Cancel" else "रद्द करें")
                }
            }
        )
    }
}
