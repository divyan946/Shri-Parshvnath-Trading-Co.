package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CustomerEnquiry
import com.example.model.QuoteItem
import androidx.compose.material.icons.filled.CreditCard
import com.example.ui.components.PaymentGatewayDialog
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.StoneBrownDark
import com.example.ui.theme.StoneBrownPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.AppStrings
import com.example.util.WhatsAppHelper
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenTab
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuoteScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val quoteItems by viewModel.quoteItems.collectAsState()
    val savedEnquiry by viewModel.customerEnquiry.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    var customerName by remember(savedEnquiry) { mutableStateOf(savedEnquiry.name) }
    var customerPhone by remember(savedEnquiry) { mutableStateOf(savedEnquiry.phone) }
    var customerAddress by remember(savedEnquiry) { mutableStateOf(savedEnquiry.address) }
    var projectType by remember(savedEnquiry) { mutableStateOf(savedEnquiry.projectType) }
    var customerNotes by remember(savedEnquiry) { mutableStateOf(savedEnquiry.notes) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    val totalEstimate = quoteItems.sumOf { it.totalPrice }
    val totalSqFt = quoteItems.sumOf { it.quantitySqFt }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val formattedTotal = currencyFormat.format(totalEstimate).replace("₹", "₹ ")

    val advance50Amount = totalEstimate * 0.5
    val balance50Amount = totalEstimate * 0.5
    val formattedAdvance = currencyFormat.format(advance50Amount).replace("₹", "₹ ")
    val formattedBalance = currencyFormat.format(balance50Amount).replace("₹", "₹ ")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 88.dp)
            .testTag("quote_screen")
    ) {
        // Title & Item Count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = AppStrings.quoteList(language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (language == AppLanguage.HI) "${quoteItems.size} आइटम जोड़े गए" else "${quoteItems.size} items in quote",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (quoteItems.isNotEmpty()) {
                TextButton(
                    onClick = { viewModel.clearQuote() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                ) {
                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (language == AppLanguage.HI) "खाली करें" else "Clear All", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Empty state or Quote items list
        if (quoteItems.isEmpty()) {
            OutlinedCard(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = AppStrings.emptyQuoteTitle(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = AppStrings.emptyQuoteSubtitle(language),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.selectTab(ScreenTab.CATALOG) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(AppStrings.btnViewProducts(language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Items List
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                quoteItems.forEach { item ->
                    QuoteItemCard(
                        item = item,
                        language = language,
                        onIncrease = { viewModel.updateQuoteQuantity(item.id, item.quantitySqFt + 50.0) },
                        onDecrease = { viewModel.updateQuoteQuantity(item.id, (item.quantitySqFt - 50.0).coerceAtLeast(10.0)) },
                        onRemove = { viewModel.removeFromQuote(item.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Estimated Total Summary Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (language == AppLanguage.HI) "कुल क्षेत्रफल:" else "Total Quantity:",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${totalSqFt.toInt()} sq ft",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.totalEstimate(language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formattedTotal,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StoneBrownPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 50% Before Delivery & 50% After Delivery Milestone Card
                    Surface(
                        color = GoldContainer,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GoldAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == AppLanguage.HI) "⭐ 50% डिलीवरी से पहले • 50% डिलीवरी के बाद" else "⭐ 50% Before Delivery • 50% After Delivery",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = StoneBrownDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.HI) "अग्रिम बुकिंग (50%)" else "Advance (50%)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = formattedAdvance,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StoneBrownDark
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (language == AppLanguage.HI) "डिलीवरी पर देय (50%)" else "Due on Delivery (50%)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = formattedBalance,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StoneBrownDark
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.HI) "*दरें एक्स-फैक्ट्री हैं। माल लोड होने से पहले 50% अग्रिम तथा साइट पर अनलोडिंग के बाद 50% भुगतान स्वीकार्य है।" else "*Ex-factory rates. 50% booking advance before factory dispatch, remaining 50% on site unloading.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Pay 50% Advance Button (UPI / Card)
            Button(
                onClick = {
                    val enquiry = CustomerEnquiry(
                        name = customerName,
                        phone = customerPhone,
                        address = customerAddress,
                        projectType = projectType,
                        notes = customerNotes
                    )
                    viewModel.saveCustomerEnquiry(enquiry)
                    showPaymentDialog = true
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("pay_advance_button")
            ) {
                Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.HI) "50% अग्रिम भुगतान करें ($formattedAdvance) • UPI / कार्ड" else "Pay 50% Advance ($formattedAdvance) • UPI / Card",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OTP Verification & Account Banner
            if (currentUser.isLoggedIn) {
                Surface(
                    color = GoldContainer.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GoldAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = StoneBrownDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.EN) "Logged In: ${currentUser.fullName}" else "लॉगिन खाता: ${currentUser.fullName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = StoneBrownDark
                            )
                            Text(
                                text = "+91 ${currentUser.phoneNumber} • ${currentUser.role.titleEn} (Verified via OTP)",
                                fontSize = 11.sp,
                                color = StoneBrownDark.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectTab(ScreenTab.ACCOUNT) }
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.EN) "Login via Mobile OTP" else "मोबाइल OTP से लॉगिन करें",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = if (language == AppLanguage.EN) "Auto-fill your details & save orders" else "विवरण स्वतः भरें व ऑर्डर ट्रैक करें",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.EN) "Login →" else "लॉगिन →",
                            color = StoneBrownPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Customer Enquiry Form Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.HI) "ग्राहक व साइट विवरण" else "Customer & Delivery Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text(if (language == AppLanguage.HI) "आपका नाम *" else "Your Full Name *") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_name_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it.filter { c -> c.isDigit() || c == '+' || c == ' ' } },
                        label = { Text(if (language == AppLanguage.HI) "फ़ोन / व्हाट्सएप नंबर *" else "Phone / WhatsApp Number *") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_phone_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customerAddress,
                        onValueChange = { customerAddress = it },
                        label = { Text(if (language == AppLanguage.HI) "डिलीवरी शहर / साइट का पता" else "Delivery City / Site Address") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Place, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_address_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.HI) "प्रोजेक्ट प्रकार:" else "Project Type:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Home / Villa", "Shop / Retail", "Commercial / Builder", "Renovation").forEach { type ->
                            FilterChip(
                                selected = projectType == type,
                                onClick = { projectType = type },
                                label = { Text(type, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customerNotes,
                        onValueChange = { customerNotes = it },
                        label = { Text(if (language == AppLanguage.HI) "विशेष निर्देश (एज पॉलिश, मोटाई, आदि)" else "Special Notes / Requirements") },
                        placeholder = { Text(if (language == AppLanguage.HI) "जैसे: डबल मोल्डिंग, तुरंत डिलीवरी चाहिए" else "e.g., edge chamfering, loading requirement") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enquiry_notes_input")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Big WhatsApp Quote Button
            Button(
                onClick = {
                    val enquiry = CustomerEnquiry(
                        name = customerName,
                        phone = customerPhone,
                        address = customerAddress,
                        projectType = projectType,
                        notes = customerNotes
                    )
                    viewModel.saveCustomerEnquiry(enquiry)
                    WhatsAppHelper.sendQuoteRequest(context, quoteItems, enquiry, totalEstimate)
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("send_quote_whatsapp_button")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.sendOnWhatsApp(language),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }

    // Payment Gateway Dialog (UPI & Credit/Debit Card with 50% advance / 50% on delivery)
    if (showPaymentDialog) {
        val enquiry = CustomerEnquiry(
            name = customerName,
            phone = customerPhone,
            address = customerAddress,
            projectType = projectType,
            notes = customerNotes
        )
        PaymentGatewayDialog(
            totalOrderAmount = totalEstimate,
            items = quoteItems,
            customerEnquiry = enquiry,
            language = language,
            onPaymentSuccess = { receipt ->
                Toast.makeText(context, "Payment of ₹${receipt.amountPaid.toInt()} confirmed! Order Ref: ${receipt.orderId}", Toast.LENGTH_LONG).show()
            },
            onDismiss = { showPaymentDialog = false }
        )
    }
}

@Composable
private fun QuoteItemCard(
    item: QuoteItem,
    language: AppLanguage,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val itemTotal = currencyFormat.format(item.totalPrice).replace("₹", "₹ ")

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.productName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Size: ${item.selectedSize} • ₹${item.pricePerSqFt.toInt()}/sq ft",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
                    }

                    Text(
                        text = "${item.quantitySqFt.toInt()} sq ft",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
                    }
                }

                // Subtotal
                Text(
                    text = itemTotal,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = StoneBrownPrimary
                )
            }
        }
    }
}
