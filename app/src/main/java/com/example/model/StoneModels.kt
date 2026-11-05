package com.example.model

enum class ProductCategory(val displayNameEn: String, val displayNameHi: String) {
    KOTA_STONE("Kota Stone", "कोटा स्टोन"),
    TILES("Tiles", "टाइल्स"),
    MARBLE("Marble", "मार्बल"),
    GRANITE("Granite", "ग्रेनाइट")
}

enum class StoneFinish(val labelEn: String, val labelHi: String) {
    ALL("All Finishes", "सभी फिनिश"),
    POLISHED("Polished / Glossy", "पॉलिश / ग्लॉसी"),
    HONED("Honed / Matte", "होन्ड / मैट"),
    NATURAL("Natural Split", "नेचुरल स्पिलिट"),
    BRUSHED("Brushed / River", "ब्रशड / रिवर")
}

enum class StoneUse(val labelEn: String, val labelHi: String) {
    ALL("All Uses", "सभी उपयोग"),
    FLOORING("Flooring", "फ्लोरिंग"),
    WALL("Wall Cladding", "दीवार"),
    KITCHEN("Kitchen Counter", "किचन"),
    BATHROOM("Bathroom", "बाथरूम"),
    OUTDOOR("Outdoor & Patio", "आउटडोर")
}

data class Product(
    val id: String,
    val name: String,
    val nameHi: String,
    val category: ProductCategory,
    val pricePerSqFt: Double,
    val availableSizes: List<String>,
    val thickness: String,
    val finish: StoneFinish,
    val uses: List<StoneUse>,
    val descriptionEn: String,
    val descriptionHi: String,
    val origin: String = "Rajasthan, India",
    val drawableRes: Int? = null,
    val previewHex: Long = 0xFF5D534A,
    val isFeatured: Boolean = false
)

data class QuoteItem(
    val id: String,
    val productId: String,
    val productName: String,
    val category: ProductCategory,
    val selectedSize: String,
    val quantitySqFt: Double,
    val pricePerSqFt: Double
) {
    val totalPrice: Double
        get() = quantitySqFt * pricePerSqFt
}

data class CustomerEnquiry(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val projectType: String = "Home / Villa",
    val notes: String = ""
)

enum class AppLanguage(val code: String, val title: String) {
    EN("en", "English"),
    HI("hi", "हिन्दी")
}

enum class PaymentMilestone(val titleEn: String, val titleHi: String, val percentage: Double) {
    ADVANCE_50("50% Before Delivery (Advance)", "50% डिलीवरी से पहले (अग्रिम राशि)", 0.50),
    FULL_100("100% Full Payment", "100% पूर्ण भुगतान", 1.0)
}

enum class PaymentMode(val titleEn: String, val titleHi: String) {
    UPI("UPI (GPay / PhonePe / Paytm / QR)", "यूपीआई (गूगल पे / फोनपे / पेटीएम)"),
    CARD("Credit / Debit Card", "क्रेडिट / डेबिट कार्ड")
}

data class PaymentReceipt(
    val transactionId: String,
    val orderId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalOrderAmount: Double,
    val amountPaid: Double,
    val balanceDue: Double,
    val milestone: PaymentMilestone,
    val paymentMode: PaymentMode,
    val customerName: String,
    val customerPhone: String,
    val itemsSummary: String
)

enum class CustomerRole(val titleEn: String, val titleHi: String) {
    HOME_BUILDER("Home Owner / Villa Builder", "मकान मालिक / विला निर्माण"),
    CONTRACTOR("Civil Contractor / Builder", "सिविल ठेकेदार / बिल्डर"),
    ARCHITECT("Architect / Interior Designer", "आर्किटेक्ट / इंटीरियर डिज़ाइनर"),
    STONE_DEALER("Stone Dealer / Wholesaler", "स्टोन डीलर / थोक व्यापारी")
}

data class UserAccount(
    val id: String = "",
    val phoneNumber: String = "",
    val fullName: String = "",
    val role: CustomerRole = CustomerRole.HOME_BUILDER,
    val deliveryCity: String = "",
    val gstin: String = "",
    val isLoggedIn: Boolean = false,
    val loginTimeMillis: Long = 0L
)

sealed class OtpStep {
    object EnterPhone : OtpStep()
    data class EnterOtp(val phone: String, val generatedOtp: String, val resendCountdownSeconds: Int) : OtpStep()
    data class Verified(val user: UserAccount) : OtpStep()
}

