package com.example.util

import com.example.model.AppLanguage

object AppStrings {

    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Shri Parshvnath Trading Co."
        AppLanguage.HI -> "श्री पार्श्वनाथ ट्रेडिंग कंपनी"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Kota Stone • Marble • Granite • Tiles"
        AppLanguage.HI -> "कोटा स्टोन • मार्बल • ग्रेनाइट • टाइल्स"
    }

    fun heroTagline(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Quality Stone & Tiles for Your Dream Space"
        AppLanguage.HI -> "आपके सपनों के आशियाने के लिए उत्तम पत्थर और टाइल्स"
    }

    fun heroSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Direct quarry sourcing from Rajasthan with precision cutting, premium finishes, and wholesale pricing for homes and mega projects."
        AppLanguage.HI -> "राजस्थान की प्रमुख खदानों से सीधा उत्तम माल, सटीक कटिंग और हर प्रोजेक्ट के लिए थोक दरें।"
    }

    fun btnViewProducts(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "View Products"
        AppLanguage.HI -> "उत्पाद देखें"
    }

    fun btnGetQuoteWhatsApp(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Get a Quote on WhatsApp"
        AppLanguage.HI -> "व्हाट्सएप पर कोटेशन पाएं"
    }

    fun whyChooseUs(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Why Choose Shri Parshvnath?"
        AppLanguage.HI -> "श्री पार्श्वनाथ को क्यों चुनें?"
    }

    fun whyQualityTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Direct Quarry Quality"
        AppLanguage.HI -> "खदान से सीधा उच्च गुणवत्ता"
    }

    fun whyQualityDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Rigorous sorting ensuring uniform color, tight density, and zero crack defect."
        AppLanguage.HI -> "सख्त गुणवत्ता जांच जिससे मिले एक समान रंग और दरार-मुक्त मजबूत पत्थर।"
    }

    fun whyPriceTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Transparent Wholesale Rates"
        AppLanguage.HI -> "पारदर्शी थोक मूल्य"
    }

    fun whyPriceDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Direct factory rates with no middlemen margins. Save up to 25% on bulk orders."
        AppLanguage.HI -> "बिना बिचौलियों के सीधे कारखाने के दाम। थोक ऑर्डर्स पर विशेष बचत।"
    }

    fun whyDeliveryTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Safe & Timely Delivery"
        AppLanguage.HI -> "सुरक्षित व समय पर डिलीवरी"
    }

    fun whyDeliveryDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Professional wooden pallet packaging and door-step transport across India."
        AppLanguage.HI -> "मजबूत पैकिंग और सुरक्षित परिवहन द्वारा सीधे आपकी साइट तक डिलीवरी।"
    }

    fun whyBulkTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Bulk & Custom Orders"
        AppLanguage.HI -> "थोक व कस्टमाइज्ड कटिंग"
    }

    fun whyBulkDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Capacity to supply 50,000+ sq ft monthly with customized thickness and chamfers."
        AppLanguage.HI -> "मासिक 50,000+ वर्ग फुट सप्लाई क्षमता और मनचाही मोटाई व एज मोल्डिंग।"
    }

    fun featuredStones(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Featured Collections"
        AppLanguage.HI -> "विशेष कलेक्शन"
    }

    fun searchPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Search stone name, color, finish, usage..."
        AppLanguage.HI -> "पत्थर का नाम, रंग, फिनिश या उपयोग खोजें..."
    }

    fun allCategories(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "All Categories"
        AppLanguage.HI -> "सभी श्रेणियां"
    }

    fun finishFilter(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Finish:"
        AppLanguage.HI -> "फिनिश:"
    }

    fun useFilter(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Usage:"
        AppLanguage.HI -> "उपयोग:"
    }

    fun addToQuote(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Add to Quote"
        AppLanguage.HI -> "कोटेशन में जोड़ें"
    }

    fun quoteList(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Quote Cart"
        AppLanguage.HI -> "कोटेशन सूची"
    }

    fun emptyQuoteTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Your quote list is empty"
        AppLanguage.HI -> "आपकी कोटेशन सूची अभी खाली है"
    }

    fun emptyQuoteSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Browse our premium Kota stone, marble, granite, or tiles collection and add items to receive an instant WhatsApp estimate."
        AppLanguage.HI -> "हमारे कोटा स्टोन, मार्बल, ग्रेनाइट या टाइल्स कैटलॉग से उत्पाद चुनें और तुरंत व्हाट्सएप कोटेशन पाएं।"
    }

    fun calculatorTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Square Feet Calculator"
        AppLanguage.HI -> "स्क्वायर फीट (वर्ग फुट) कैलकुलेटर"
    }

    fun calculatorSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Enter your room dimensions. We recommend adding 10% for cutting waste and perimeter edge adjustments."
        AppLanguage.HI -> "कमरे की लंबाई और चौड़ाई दर्ज करें। कटिंग और किनारों की वेस्टेज के लिए 10% अतिरिक्त जोड़ने की सलाह दी जाती है।"
    }

    fun describeWithAi(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Describe with AI"
        AppLanguage.HI -> "AI से विवरण व उपयोग जानिए"
    }

    fun sendOnWhatsApp(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Send Quote Request on WhatsApp"
        AppLanguage.HI -> "व्हाट्सएप पर कोटेशन भेजें"
    }

    fun totalEstimate(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Estimated Material Total:"
        AppLanguage.HI -> "अनुमानित कुल सामग्री मूल्य:"
    }

    fun navHome(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Home"
        AppLanguage.HI -> "होम"
    }

    fun navCatalog(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Catalog"
        AppLanguage.HI -> "उत्पाद"
    }

    fun navCalculator(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Calculator"
        AppLanguage.HI -> "कैलकुलेटर"
    }

    fun navQuote(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Quote"
        AppLanguage.HI -> "कोटेशन"
    }

    fun navAbout(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "About & Contact"
        AppLanguage.HI -> "संपर्क"
    }

    fun navAccount(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "My Account"
        AppLanguage.HI -> "मेरा खाता"
    }

    fun loginTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Customer Login"
        AppLanguage.HI -> "ग्राहक लॉगिन"
    }

    fun loginSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Enter your mobile number to receive a one-time OTP for instant verification & orders."
        AppLanguage.HI -> "तुरंत सत्यापन और ऑर्डर के लिए अपना मोबाइल नंबर दर्ज कर OTP प्राप्त करें।"
    }

    fun verifyOtpTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Verify Mobile OTP"
        AppLanguage.HI -> "मोबाइल OTP सत्यापित करें"
    }

    fun logoutTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Logout"
        AppLanguage.HI -> "लॉगआउट"
    }

    fun logoutConfirm(lang: AppLanguage) = when (lang) {
        AppLanguage.EN -> "Are you sure you want to log out? Your saved quotes will remain secure on this device."
        AppLanguage.HI -> "क्या आप लॉगआउट करना चाहते हैं? आपके सेव किए गए कोटेशन इस डिवाइस में सुरक्षित रहेंगे।"
    }
}
