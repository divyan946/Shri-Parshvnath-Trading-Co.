package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.model.CustomerEnquiry
import com.example.model.QuoteItem
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.Locale

object WhatsAppHelper {

    // Official business credentials from GST Invoice
    const val BUSINESS_PHONE = "919413085910"
    const val DISPLAY_PHONE = "+91 94130 85910"
    const val SECONDARY_PHONE = "+91 92143 85910"
    const val BUSINESS_EMAIL = "nileshj24679@gmail.com"
    const val GSTIN = "08AIHPJ8128R1ZI"
    const val SHOP_ADDRESS = "Bazar No. 1, Ramganjmandi - 326519 (Distt. Kota, Rajasthan)"
    const val SPECIALTY = "Suppliers of : Diamond Cut Rough & Polished KOTA STONE & Lime Stone"

    fun sendQuoteRequest(
        context: Context,
        items: List<QuoteItem>,
        enquiry: CustomerEnquiry,
        totalEstimate: Double
    ) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val formattedTotal = currencyFormat.format(totalEstimate).replace("₹", "₹ ")

        val messageBuilder = StringBuilder()
        messageBuilder.appendLine("🏛️ *NEW ENQUIRY - SHRI PARSHAVNATH TRADING CO.*")
        messageBuilder.appendLine("📍 Ramganjmandi - 326519 (Distt. Kota, Raj.) • GSTIN: 08AIHPJ8128R1ZI")
        messageBuilder.appendLine("━━━━━━━━━━━━━━━━━━━━━")
        messageBuilder.appendLine("👤 *CUSTOMER DETAILS:*")
        messageBuilder.appendLine("• Name: ${enquiry.name.ifBlank { "Valued Customer" }}")
        messageBuilder.appendLine("• Phone: ${enquiry.phone.ifBlank { "Not provided" }}")
        if (enquiry.address.isNotBlank()) {
            messageBuilder.appendLine("• Delivery Location: ${enquiry.address}")
        }
        messageBuilder.appendLine("• Project Type: ${enquiry.projectType}")
        if (enquiry.notes.isNotBlank()) {
            messageBuilder.appendLine("• Notes / Specs: ${enquiry.notes}")
        }

        messageBuilder.appendLine()
        messageBuilder.appendLine("📋 *SELECTED STONE & TILE ITEMS:*")

        var totalArea = 0.0
        items.forEachIndexed { index, item ->
            totalArea += item.quantitySqFt
            val itemTotal = currencyFormat.format(item.totalPrice).replace("₹", "₹ ")
            messageBuilder.appendLine("${index + 1}. *${item.productName}*")
            messageBuilder.appendLine("   ▪ Size: ${item.selectedSize}")
            messageBuilder.appendLine("   ▪ Quantity: ${item.quantitySqFt.toInt()} sq ft @ ₹${item.pricePerSqFt.toInt()}/sq ft")
            messageBuilder.appendLine("   ▪ Subtotal: $itemTotal")
        }

        messageBuilder.appendLine("━━━━━━━━━━━━━━━━━━━━━")
        messageBuilder.appendLine("📐 *Total Area:* ${totalArea.toInt()} sq ft")
        messageBuilder.appendLine("💰 *ESTIMATED MATERIAL TOTAL:* $formattedTotal")
        messageBuilder.appendLine()
        messageBuilder.appendLine("Kindly share availability, freight / transport charges to my location, and sample dispatch details. Thank you!")

        val rawText = messageBuilder.toString()
        openWhatsApp(context, BUSINESS_PHONE, rawText)
    }

    fun sendDirectEnquiry(context: Context, stoneName: String? = null) {
        val text = if (stoneName != null) {
            "Hello Shri Parshvnath Trading Co., I am interested in inquiring about *$stoneName*. Please provide current wholesale prices, available slab sizes, and delivery schedule."
        } else {
            "Hello Shri Parshvnath Trading Co., I would like to get a quote for Kota Stone, Marble, Granite, and Tiles for my project."
        }
        openWhatsApp(context, BUSINESS_PHONE, text)
    }

    private fun openWhatsApp(context: Context, phoneNumber: String, message: String) {
        try {
            val encodedMessage = URLEncoder.encode(message, "UTF-8")
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=$encodedMessage"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp. Please ensure WhatsApp is installed or visit whatsapp.com", Toast.LENGTH_LONG).show()
        }
    }
}
