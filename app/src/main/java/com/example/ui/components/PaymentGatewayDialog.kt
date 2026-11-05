package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.CustomerEnquiry
import com.example.model.PaymentMilestone
import com.example.model.PaymentMode
import com.example.model.PaymentReceipt
import com.example.model.QuoteItem
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.StoneBrownDark
import com.example.ui.theme.StoneBrownPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.PaymentHelper
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

@Composable
fun PaymentGatewayDialog(
    totalOrderAmount: Double,
    items: List<QuoteItem>,
    customerEnquiry: CustomerEnquiry,
    language: AppLanguage,
    onPaymentSuccess: (PaymentReceipt) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    // Milestone selection: 50% advance vs 100% full
    var selectedMilestone by remember { mutableStateOf(PaymentMilestone.ADVANCE_50) }
    var selectedPaymentMode by remember { mutableStateOf(PaymentMode.UPI) }

    val amountToPay = totalOrderAmount * selectedMilestone.percentage
    val balanceDue = totalOrderAmount - amountToPay

    val formattedAmountToPay = currencyFormat.format(amountToPay).replace("₹", "₹ ")
    val formattedBalanceDue = currencyFormat.format(balanceDue).replace("₹", "₹ ")
    val formattedTotal = currencyFormat.format(totalOrderAmount).replace("₹", "₹ ")

    // Card Input States
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf(customerEnquiry.name) }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    // Processing & Success States
    var isProcessing by remember { mutableStateOf(false) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var generatedReceipt by remember { mutableStateOf<PaymentReceipt?>(null) }

    val orderId = remember { "SP-ORD-${Random.nextInt(100000, 999999)}" }

    Dialog(
        onDismissRequest = {
            if (!isProcessing) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .testTag("payment_gateway_dialog")
        ) {
            if (generatedReceipt != null) {
                // Receipt Success Screen
                PaymentReceiptView(
                    receipt = generatedReceipt!!,
                    language = language,
                    onShareWhatsApp = {
                        PaymentHelper.sendPaymentConfirmationOnWhatsApp(context, generatedReceipt!!)
                    },
                    onClose = {
                        onPaymentSuccess(generatedReceipt!!)
                        onDismiss()
                    }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(StoneBrownDark)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.HI) "सुरक्षित भुगतान गेटवे (50% अग्रिम)" else "Secure Payment Gateway",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {

                        // 50% Before Delivery & 50% After Delivery Split Banner
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = GoldContainer),
                            border = BorderStroke(1.dp, GoldAccent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.HI) "⭐ 50-50 डिलीवरी भुगतान नीति" else "⭐ 50-50 Delivery Milestone Model",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = StoneBrownDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.HI)
                                        "पत्थर व टाइल्स ऑर्डर्स के लिए 50% अग्रिम डिलीवरी से पहले और शेष 50% आपकी साइट पर माल पहुंचने व अनलोडिंग के बाद लिया जाता है।"
                                    else
                                        "For stone & tile logistics: 50% advance before factory dispatch, remaining 50% paid on site unloading and inspection.",
                                    fontSize = 12.sp,
                                    color = StoneBrownDark.copy(alpha = 0.9f),
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = selectedMilestone == PaymentMilestone.ADVANCE_50,
                                        onClick = { selectedMilestone = PaymentMilestone.ADVANCE_50 },
                                        label = {
                                            Text(
                                                if (language == AppLanguage.HI) "50% अग्रिम ($formattedAmountToPay)" else "Pay 50% Advance ($formattedAmountToPay)",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    )

                                    FilterChip(
                                        selected = selectedMilestone == PaymentMilestone.FULL_100,
                                        onClick = { selectedMilestone = PaymentMilestone.FULL_100 },
                                        label = {
                                            Text(
                                                if (language == AppLanguage.HI) "100% पूरा भुगतान" else "Pay 100% Full",
                                                fontSize = 12.sp
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Amount Breakdown Card
                        OutlinedCard(
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.HI) "कुल ऑर्डर मूल्य:" else "Total Order Value:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(text = formattedTotal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }

                                if (selectedMilestone == PaymentMilestone.ADVANCE_50) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.HI) "डिलीवरी के बाद देय राशि (50%):" else "Balance Due After Delivery (50%):",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(text = formattedBalanceDue, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.HI) "अभी भुगतान करें (देय राशि):" else "Payable Amount Now:",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = formattedAmountToPay,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = StoneBrownPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Payment Method Tabs (UPI vs Card)
                        TabRow(
                            selectedTabIndex = if (selectedPaymentMode == PaymentMode.UPI) 0 else 1,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Tab(
                                selected = selectedPaymentMode == PaymentMode.UPI,
                                onClick = { selectedPaymentMode = PaymentMode.UPI },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("UPI Apps & QR", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            )

                            Tab(
                                selected = selectedPaymentMode == PaymentMode.CARD,
                                onClick = { selectedPaymentMode = PaymentMode.CARD },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Debit / Credit Card", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // TAB CONTENT: UPI
                        if (selectedPaymentMode == PaymentMode.UPI) {
                            Column {
                                // 1. Direct UPI App Launch Button
                                Button(
                                    onClick = {
                                        PaymentHelper.launchUpiPayment(
                                            context = context,
                                            amount = amountToPay,
                                            orderId = orderId,
                                            note = "Advance 50% Stone Order - $orderId"
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("launch_upi_app_button")
                                ) {
                                    Icon(imageVector = Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.HI) "GPay / PhonePe / Paytm से $formattedAmountToPay दें" else "Pay $formattedAmountToPay via Any UPI App",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // 2. UPI VPA & QR Card for Manual Transfer
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.HI) "या मर्चेंट UPI ID पर सीधे ट्रांसफर करें" else "Or Transfer Directly to Business UPI ID",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // UPI ID Badge with Copy Button
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, GoldAccent),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "Payee: ${PaymentHelper.MERCHANT_NAME}",
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Text(
                                                        text = PaymentHelper.DEFAULT_UPI_ID,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 15.sp,
                                                        color = StoneBrownPrimary
                                                    )
                                                }

                                                OutlinedButton(
                                                    onClick = {
                                                        clipboardManager.setText(AnnotatedString(PaymentHelper.DEFAULT_UPI_ID))
                                                        Toast.makeText(context, "UPI ID copied: ${PaymentHelper.DEFAULT_UPI_ID}", Toast.LENGTH_SHORT).show()
                                                    },
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Copy", fontSize = 11.sp)
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        // "Confirm UPI Payment Received"
                                        OutlinedButton(
                                            onClick = {
                                                val summary = items.joinToString("\n") { "• ${it.productName} (${it.selectedSize}): ${it.quantitySqFt.toInt()} sq ft" }
                                                val receipt = PaymentReceipt(
                                                    transactionId = "UPI-${Random.nextInt(10000000, 99999999)}",
                                                    orderId = orderId,
                                                    totalOrderAmount = totalOrderAmount,
                                                    amountPaid = amountToPay,
                                                    balanceDue = balanceDue,
                                                    milestone = selectedMilestone,
                                                    paymentMode = PaymentMode.UPI,
                                                    customerName = customerEnquiry.name.ifBlank { "Valued Customer" },
                                                    customerPhone = customerEnquiry.phone.ifBlank { "9413085910" },
                                                    itemsSummary = summary
                                                )
                                                generatedReceipt = receipt
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = WhatsAppGreen, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (language == AppLanguage.HI) "मैंने UPI भुगतान कर दिया है (रसीद बनाएं)" else "I Have Completed UPI Payment (Generate Receipt)",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // TAB CONTENT: DEBIT / CREDIT CARD
                            Column {
                                OutlinedTextField(
                                    value = cardNumber,
                                    onValueChange = { input ->
                                        val digits = input.filter { it.isDigit() }.take(16)
                                        cardNumber = PaymentHelper.formatCardNumber(digits)
                                    },
                                    label = { Text("Card Number (16 Digits)") },
                                    placeholder = { Text("XXXX XXXX XXXX XXXX") },
                                    leadingIcon = { Icon(imageVector = Icons.Default.CreditCard, contentDescription = null) },
                                    trailingIcon = {
                                        val rawDigits = cardNumber.filter { it.isDigit() }
                                        val brand = PaymentHelper.detectCardBrand(rawDigits)
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.padding(end = 8.dp)
                                        ) {
                                            Text(
                                                text = brand,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("card_number_input")
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = cardHolder,
                                    onValueChange = { cardHolder = it },
                                    label = { Text("Cardholder Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = cardExpiry,
                                        onValueChange = { input ->
                                            val digits = input.filter { it.isDigit() }.take(4)
                                            cardExpiry = if (digits.length >= 2) "${digits.take(2)}/${digits.drop(2)}" else digits
                                        },
                                        label = { Text("Expiry (MM/YY)") },
                                        placeholder = { Text("12/28") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f)
                                    )

                                    OutlinedTextField(
                                        value = cardCvv,
                                        onValueChange = { cardCvv = it.filter { c -> c.isDigit() }.take(3) },
                                        label = { Text("CVV") },
                                        placeholder = { Text("•••") },
                                        visualTransformation = PasswordVisualTransformation(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "256-bit Encrypted SSL Gateway • RBI 3D-Secure Protected",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        val cleanDigits = cardNumber.filter { it.isDigit() }
                                        if (cleanDigits.length < 16) {
                                            Toast.makeText(context, "Please enter a valid 16-digit card number", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        if (cardExpiry.length < 5) {
                                            Toast.makeText(context, "Please enter valid expiry date (MM/YY)", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        if (cardCvv.length < 3) {
                                            Toast.makeText(context, "Please enter 3-digit CVV", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        showOtpDialog = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("pay_card_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.HI) "कार्ड से $formattedAmountToPay सुरक्षित भुगतान करें" else "Pay $formattedAmountToPay Securely with Card",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 3D Secure / OTP Simulation Dialog for Card Transactions
    if (showOtpDialog) {
        Dialog(onDismissRequest = { if (!isProcessing) showOtpDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = StoneBrownPrimary, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bank 3D-Secure Authorization",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "An authorization OTP was sent to your registered mobile ending in ••89",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Amount: $formattedAmountToPay",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = StoneBrownPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { otpInput = it.filter { c -> c.isDigit() }.take(6) },
                        label = { Text("Enter 6-digit Bank OTP") },
                        placeholder = { Text("123456") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isProcessing) {
                        CircularProgressIndicator(color = StoneBrownPrimary, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Authorizing with issuing bank...", fontSize = 12.sp)
                    } else {
                        Button(
                            onClick = {
                                isProcessing = true
                                // Simulate bank settlement
                                val summary = items.joinToString("\n") { "• ${it.productName} (${it.selectedSize}): ${it.quantitySqFt.toInt()} sq ft" }
                                val receipt = PaymentReceipt(
                                    transactionId = "TXN-CARD-${Random.nextInt(10000000, 99999999)}",
                                    orderId = orderId,
                                    totalOrderAmount = totalOrderAmount,
                                    amountPaid = amountToPay,
                                    balanceDue = balanceDue,
                                    milestone = selectedMilestone,
                                    paymentMode = PaymentMode.CARD,
                                    customerName = cardHolder.ifBlank { "Valued Customer" },
                                    customerPhone = customerEnquiry.phone.ifBlank { "9413085910" },
                                    itemsSummary = summary
                                )
                                isProcessing = false
                                showOtpDialog = false
                                generatedReceipt = receipt
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = StoneBrownPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit OTP & Confirm Payment")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentReceiptView(
    receipt: PaymentReceipt,
    language: AppLanguage,
    onShareWhatsApp: () -> Unit,
    onClose: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val formattedPaid = currencyFormat.format(receipt.amountPaid).replace("₹", "₹ ")
    val formattedBalance = currencyFormat.format(receipt.balanceDue).replace("₹", "₹ ")
    val formattedTotal = currencyFormat.format(receipt.totalOrderAmount).replace("₹", "₹ ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = Color(0xFFE8F5E9),
            shape = CircleShape,
            modifier = Modifier.size(70.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = WhatsAppGreen,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (language == AppLanguage.HI) "अग्रिम भुगतान सफल!" else "Payment Successful!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = StoneBrownDark
        )

        Text(
            text = if (language == AppLanguage.HI) "श्री पार्श्वनाथ ट्रेडिंग कंपनी के पास आपका ऑर्डर बुक हो गया है।" else "Your advance payment has been confirmed with Shri Parshvnath Trading Co.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Receipt Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                ReceiptRow("Transaction ID", receipt.transactionId)
                ReceiptRow("Order Reference", receipt.orderId)
                ReceiptRow("Payment Mode", receipt.paymentMode.name)
                ReceiptRow("Milestone", if (receipt.milestone == PaymentMilestone.ADVANCE_50) "50% Advance Before Delivery" else "100% Full Payment")

                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
                Spacer(modifier = Modifier.height(8.dp))

                ReceiptRow("Amount Paid Now", formattedPaid, isBold = true, highlightColor = WhatsAppGreen)
                if (receipt.milestone == PaymentMilestone.ADVANCE_50) {
                    ReceiptRow("Balance on Delivery (50%)", formattedBalance, isBold = true, highlightColor = StoneBrownPrimary)
                }
                ReceiptRow("Total Order Value", formattedTotal)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action: Share Receipt on WhatsApp to 9413085910
        Button(
            onClick = onShareWhatsApp,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("share_payment_whatsapp_button")
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.HI) "व्हाट्सएप पर भुगतान रसीद भेजें (9413085910)" else "Send Payment Receipt on WhatsApp (9413085910)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onClose,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (language == AppLanguage.HI) "समाप्त करें" else "Close Receipt")
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlightColor: Color? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = if (isBold) 14.sp else 12.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = highlightColor ?: MaterialTheme.colorScheme.onSurface
        )
    }
}
