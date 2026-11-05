package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.model.PaymentMilestone
import com.example.model.PaymentMode
import com.example.model.PaymentReceipt
import java.net.URLEncoder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PaymentHelper {

    const val DEFAULT_UPI_ID = "9413085910@upi"
    const val MERCHANT_NAME = "Shri Parshvnath Trading Co."

    fun launchUpiPayment(
        context: Context,
        amount: Double,
        orderId: String,
        note: String
    ) {
        try {
            val formattedAmount = String.format(Locale.US, "%.2f", amount)
            val encodedMerchant = URLEncoder.encode(MERCHANT_NAME, "UTF-8")
            val encodedNote = URLEncoder.encode(note, "UTF-8")

            val upiUriString = "upi://pay?pa=$DEFAULT_UPI_ID&pn=$encodedMerchant&am=$formattedAmount&cu=INR&tn=$encodedNote&tr=$orderId"
            val uri = Uri.parse(upiUriString)

            val intent = Intent(Intent.ACTION_VIEW, uri)
            val chooser = Intent.createChooser(intent, "Pay ₹${formattedAmount.toInt()} via UPI (Google Pay, PhonePe, Paytm, BHIM)")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "No UPI app found. Please use the UPI ID: $DEFAULT_UPI_ID or pay with Credit/Debit card.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun sendPaymentConfirmationOnWhatsApp(
        context: Context,
        receipt: PaymentReceipt
    ) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val paidFormatted = currencyFormat.format(receipt.amountPaid).replace("₹", "₹ ")
        val balanceFormatted = currencyFormat.format(receipt.balanceDue).replace("₹", "₹ ")
        val totalFormatted = currencyFormat.format(receipt.totalOrderAmount).replace("₹", "₹ ")
        val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(receipt.timestamp))

        val message = """
            💳 *PAYMENT RECEIPT - SHRI PARSHAVNATH TRADING CO.*
            📍 Bazar No. 1, Ramganjmandi - 326519 (Distt. Kota, Raj.)
            📑 GSTIN: 08AIHPJ8128R1ZI
            ━━━━━━━━━━━━━━━━━━━━━
            🧾 *Transaction ID:* ${receipt.transactionId}
            📦 *Order Ref:* ${receipt.orderId}
            📅 *Date & Time:* $dateFormatted
            
            👤 *CUSTOMER DETAILS:*
            • Name: ${receipt.customerName.ifBlank { "Valued Customer" }}
            • Phone: ${receipt.customerPhone.ifBlank { "9413085910" }}
            
            💰 *PAYMENT BREAKDOWN (50-50 MILESTONE):*
            • Payment Mode: ${receipt.paymentMode.name}
            • Milestone: ${if (receipt.milestone == PaymentMilestone.ADVANCE_50) "50% Advance Before Delivery" else "100% Full Payment"}
            • *Amount Paid Now:* $paidFormatted ✅
            • *Balance Due After Delivery:* $balanceFormatted
            • Total Material Value: $totalFormatted
            
            📋 *ORDER SUMMARY:*
            ${receipt.itemsSummary}
            
            ━━━━━━━━━━━━━━━━━━━━━
            Kindly confirm payment receipt at your accounts desk and initiate quarry loading and transport dispatch to my site. Thank you!
        """.trimIndent()

        val encoded = URLEncoder.encode(message, "UTF-8")
        val url = "https://api.whatsapp.com/send?phone=${WhatsAppHelper.BUSINESS_PHONE}&text=$encoded"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "Could not open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    // Card format helpers
    fun formatCardNumber(input: String): String {
        val digits = input.filter { it.isDigit() }.take(16)
        return digits.chunked(4).joinToString(" ")
    }

    fun detectCardBrand(digits: String): String {
        return when {
            digits.startsWith("4") -> "Visa"
            digits.startsWith("5") || digits.startsWith("2") -> "Mastercard"
            digits.startsWith("60") || digits.startsWith("65") || digits.startsWith("81") || digits.startsWith("82") || digits.startsWith("508") -> "RuPay"
            digits.startsWith("34") || digits.startsWith("37") -> "Amex"
            else -> "Card"
        }
    }
}
