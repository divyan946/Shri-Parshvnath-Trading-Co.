package com.example.data

import com.example.R
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.StoneFinish
import com.example.model.StoneUse

/**
 * Shri Parshvnath Trading Co. - Centralized Product Catalog
 * 
 * Edit this file directly to add new stones, modify square-foot pricing,
 * update standard sizes, or attach custom photos and textures.
 */
object ProductCatalog {

    val SAMPLE_PRODUCTS: List<Product> = listOf(
        // ================= KOTA STONE =================
        Product(
            id = "kota_blue_natural",
            name = "Kota Blue Natural Stone",
            nameHi = "कोटा ब्लू नेचुरल स्टोन",
            category = ProductCategory.KOTA_STONE,
            pricePerSqFt = 28.0,
            availableSizes = listOf("11x11 inch", "17x11 inch", "23x23 inch", "2x2 ft", "2x4 ft"),
            thickness = "22 - 30 mm (Hand cut / Split)",
            finish = StoneFinish.NATURAL,
            uses = listOf(StoneUse.OUTDOOR, StoneUse.FLOORING),
            descriptionEn = "Authentic fine-grained dolomitic limestone quarried in Kota, Rajasthan. Naturally cleft surface offers superior grip, cooling properties under scorching sun, and lifetime resistance against frost and heavy vehicular traffic.",
            descriptionHi = "राजस्थान के कोटा से सीधे खनन किया गया प्रामाणिक डोलोमाइट चूना पत्थर। प्राकृतिक खुरदरी सतह धूप में भी ठंडी रहती है और भारी ट्रैफिक और बारिश में फिसलन-मुक्त रहती है।",
            origin = "Ramganj Mandi, Kota, Rajasthan",
            previewHex = 0xFF4A5568, // Slate bluish grey
            drawableRes = R.drawable.gallery_outdoor_patio,
            isFeatured = true
        ),
        Product(
            id = "kota_brown_polished",
            name = "Kota Brown Polished Slab",
            nameHi = "कोटा ब्राउन पॉलिश्ड स्लैब",
            category = ProductCategory.KOTA_STONE,
            pricePerSqFt = 36.0,
            availableSizes = listOf("2x2 ft", "2x3 ft", "2x4 ft", "Custom Slabs up to 6 ft"),
            thickness = "18 - 22 mm (Machine Calibrated)",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.FLOORING, StoneUse.WALL),
            descriptionEn = "Warm earthy chocolate brown Kota limestone polished to a soft satin mirror finish. Outstanding density with non-porous structure, making it ideal for hospital corridors, schools, and elegant rustic interior living floors.",
            descriptionHi = "गहरे चॉकलेटी भूरे रंग का कोटा स्टोन, जो मशीन से समतल और दर्पण जैसी पॉलिश से तैयार किया जाता है। अस्पताल, स्कूल और आधुनिक घरों के लिविंग रूम के लिए सर्वश्रेष्ठ।",
            origin = "Suket Mines, Kota, Rajasthan",
            previewHex = 0xFF5D4037, // Earthy brown
            drawableRes = null,
            isFeatured = true
        ),
        Product(
            id = "kota_green_honed",
            name = "Kota Green Honed Stone",
            nameHi = "कोटा ग्रीन होन्ड स्टोन",
            category = ProductCategory.KOTA_STONE,
            pricePerSqFt = 32.0,
            availableSizes = listOf("1x1 ft", "1.5x1.5 ft", "2x2 ft", "2x2.5 ft"),
            thickness = "15 - 20 mm",
            finish = StoneFinish.HONED,
            uses = listOf(StoneUse.FLOORING, StoneUse.BATHROOM, StoneUse.OUTDOOR),
            descriptionEn = "Smooth velvet-matte honed Kota green stone. Calibrated for seamless jointing. Provides a calm, soothing sage-green ambience for verandas, traditional courtyards, and wet area pathways.",
            descriptionHi = "मुलायम मैट फिनिश वाला कोटा ग्रीन स्टोन। फिसलन रहित और आसान रखरखाव के साथ बरामदे, आंगन और गीले क्षेत्रों के लिए अत्यंत उपयुक्त।",
            origin = "Ramganj Mandi, Kota, Rajasthan",
            previewHex = 0xFF3E4F42, // Sage olive stone green
            drawableRes = null,
            isFeatured = false
        ),

        // ================= MARBLE =================
        Product(
            id = "makrana_white_marble",
            name = "Makrana Pure White Marble",
            nameHi = "मकराना शुद्ध सफेद मार्बल",
            category = ProductCategory.MARBLE,
            pricePerSqFt = 180.0,
            availableSizes = listOf("3x2 ft", "4x3 ft", "Gang-saw Slabs 7x5 ft", "Jumbo Slabs"),
            thickness = "16 - 18 mm",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.FLOORING, StoneUse.WALL),
            descriptionEn = "The world-renowned calcitic marble used in the Taj Mahal. 98% pure calcium carbonate that never yellows with age and sparkles like sugar crystals. Retains radiant cooling and grows brighter with daily usage.",
            descriptionHi = "ताजमहल की पहचान मकराना का शुद्ध सफेद मार्बल। 98% शुद्ध कैल्साइट जो कभी पीला नहीं पड़ता और समय के साथ और अधिक चमकता है। मंदिर और शाही लिविंग रूम के लिए।",
            origin = "Makrana, Nagaur, Rajasthan",
            previewHex = 0xFFF0F4F8, // Pristine white with crystalline glow
            drawableRes = R.drawable.hero_stone_banner,
            isFeatured = true
        ),
        Product(
            id = "italian_statuario_marble",
            name = "Italian-Style Statuario Marble",
            nameHi = "इटैलियन स्टाइल स्टैच्यूएरियो मार्बल",
            category = ProductCategory.MARBLE,
            pricePerSqFt = 260.0,
            availableSizes = listOf("Custom Slabs 8x5 ft", "Book-match Slabs 9x6 ft"),
            thickness = "18 mm (Epoxy treated)",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.FLOORING, StoneUse.WALL, StoneUse.BATHROOM),
            descriptionEn = "Breathtaking bright white background adorned with bold, dramatic charcoal-grey feather veining. Cut for book-matched symmetry across expansive master suites and architectural focal walls.",
            descriptionHi = "दूधिया सफेद पृष्ठभूमि पर आकर्षक ग्रे रेशों वाला लक्ज़री मार्बल। बुक-मैच पैटर्न में ड्राइंग रूम, लग्जरी बेडरूम और लॉबी की दीवारों के लिए पहली पसंद।",
            origin = "Premium Selection (Custom Processed)",
            previewHex = 0xFFECEFF1, // Statuario grey white veined
            drawableRes = null,
            isFeatured = true
        ),
        Product(
            id = "rainforest_green_marble",
            name = "Rainforest Green Marble",
            nameHi = "रेनफॉरेस्ट ग्रीन मार्बल (बिदासर)",
            category = ProductCategory.MARBLE,
            pricePerSqFt = 140.0,
            availableSizes = listOf("2x2 ft", "Slabs 6x4 ft", "Custom Cut"),
            thickness = "18 mm",
            finish = StoneFinish.BRUSHED,
            uses = listOf(StoneUse.WALL, StoneUse.KITCHEN, StoneUse.BATHROOM),
            descriptionEn = "Exotic serpentinite marble quarried in Bidasar, Rajasthan. Intertwined deep forest green and golden-brown tree-branch veins create an enchanting natural landscape canvas for TV panels, fireplace claddings, and luxury bath vanities.",
            descriptionHi = "बिदासर का प्रसिद्ध प्राकृतिक सर्पेंटिनाइट मार्बल। घने जंगल की शाखाओं जैसे सुनहरे-भूरे व हरे पैटर्न जो हर दीवार और वैनिटी को कलाकृति बना देते हैं।",
            origin = "Bidasar, Churu, Rajasthan",
            previewHex = 0xFF2E4032, // Rainforest green
            drawableRes = null,
            isFeatured = false
        ),

        // ================= GRANITE =================
        Product(
            id = "black_galaxy_granite",
            name = "Black Galaxy Granite",
            nameHi = "ब्लैक गैलेक्सी ग्रेनाइट",
            category = ProductCategory.GRANITE,
            pricePerSqFt = 190.0,
            availableSizes = listOf("Gangsaw Slabs 8x3 ft", "Kitchen Counter Size 10x2.5 ft", "2x2 ft"),
            thickness = "18 - 20 mm",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.KITCHEN, StoneUse.FLOORING, StoneUse.WALL),
            descriptionEn = "Deep pitch-black granite studded with naturally twinkling golden-copper bronzite flakes. Extremely scratch, heat, and lemon-acid resistant. The gold standard for modern modular kitchen countertops and executive tabletops.",
            descriptionHi = "चमकते सुनहरे कणों से जड़ा गहरा काला ग्रेनाइट। गर्म बर्तनों, खट्टे मसालों और खरोंच से पूरी तरह सुरक्षित। मॉड्यूलर किचन काउंटरटॉप्स के लिए सबसे लोकप्रिय।",
            origin = "Ongole / Chimakurthy, AP",
            previewHex = 0xFF141416, // Pitch black with gold sparkles
            drawableRes = null,
            isFeatured = true
        ),
        Product(
            id = "rajasthan_pink_granite",
            name = "Rajasthan Pink Granite (Chima)",
            nameHi = "राजस्थान पिंक ग्रेनाइट (चीमा)",
            category = ProductCategory.GRANITE,
            pricePerSqFt = 75.0,
            availableSizes = listOf("Stair Steps 4x1 ft", "6x2.5 ft", "8x3 ft Slabs"),
            thickness = "18 mm",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.FLOORING, StoneUse.OUTDOOR, StoneUse.WALL),
            descriptionEn = "Uniform pastel pink igneous granite with consistent grey and black micro-crystals. Known for unmatched durability, high compressive strength, and uniform appearance over thousands of square feet. Excellent for staircase treads, parking, and public buildings.",
            descriptionHi = "गुलाबी रंग का मजबूत चीमा ग्रेनाइट। भारी वजन और तेज धूप में भी रंग नहीं बदलता। सीढ़ियों, बाहरी गलियारों और व्यावसायिक इमारतों के लिए किफायती और टिकाऊ।",
            origin = "Jalore, Rajasthan",
            previewHex = 0xFFD7A79D, // Chima pink stone
            drawableRes = null,
            isFeatured = false
        ),
        Product(
            id = "tan_brown_granite",
            name = "Tan Brown Granite",
            nameHi = "टैन ब्राउन ग्रेनाइट",
            category = ProductCategory.GRANITE,
            pricePerSqFt = 115.0,
            availableSizes = listOf("Slabs 7x3 ft", "Kitchen Slabs 9x2.5 ft", "2x2 ft"),
            thickness = "18 - 20 mm",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.KITCHEN, StoneUse.FLOORING),
            descriptionEn = "Rich dark chocolate background with swirling terracotta, burgundy, and burnt orange floral crystals. Masks water spots effortlessly and pairs gorgeously with wooden and metallic cabinetry.",
            descriptionHi = "चॉकलेट और गहरे कत्थई रंग का खूबसूरत ग्रेनाइट। पानी या तेल के दाग नहीं दिखते और लकड़ी के फर्नीचर के साथ शानदार कॉम्बिनेशन बनाता है।",
            origin = "Karimnagar, Telangana",
            previewHex = 0xFF4A342B, // Tan brown
            drawableRes = null,
            isFeatured = false
        ),

        // ================= TILES =================
        Product(
            id = "gvt_marble_tile",
            name = "GVT Marble Floor Tiles 600x1200 mm",
            nameHi = "जीवीटी मार्बल फ्लोर टाइल्स (600x1200 मिमी)",
            category = ProductCategory.TILES,
            pricePerSqFt = 65.0,
            availableSizes = listOf("600x1200 mm (2x4 ft)", "800x1600 mm (2.6x5.2 ft)"),
            thickness = "9 mm (Rectified Edge)",
            finish = StoneFinish.POLISHED,
            uses = listOf(StoneUse.FLOORING, StoneUse.WALL),
            descriptionEn = "Large format Glazed Vitrified Tiles reproducing Italian Calacatta marble with high-definition digital inkjet glaze. Nano-polished, stain-proof, and impervious to water absorption (<0.05%).",
            descriptionHi = "भव्य 2x4 फीट के बड़े आकार वाली ग्लेज्ड विट्रिफाइड टाइल्स। इतालवी मार्बल का हूबहू लुक, बिना दाग-धब्बे का डर और बेहद आसान सफाई।",
            origin = "Morbi, Gujarat",
            previewHex = 0xFFE0E5EC, // High gloss marble tile
            drawableRes = null,
            isFeatured = true
        ),
        Product(
            id = "moroccan_wall_tile",
            name = "Rustic Moroccan Wall Tiles 300x600 mm",
            nameHi = "मोरक्कन आर्टिसन वॉल टाइल्स (300x600 मिमी)",
            category = ProductCategory.TILES,
            pricePerSqFt = 52.0,
            availableSizes = listOf("300x600 mm (1x2 ft)", "300x450 mm"),
            thickness = "8.5 mm",
            finish = StoneFinish.HONED,
            uses = listOf(StoneUse.WALL, StoneUse.KITCHEN, StoneUse.BATHROOM),
            descriptionEn = "Artisanal ceramic digital wall tiles featuring timeless Mediterranean geometric patterns and subtle weathered textures. Perfect for vibrant kitchen backsplashes, shower accent niches, and boutique cafe restrooms.",
            descriptionHi = "कलात्मक मोरक्कन ज्यामितीय पैटर्न वाली दीवार टाइलें। किचन बैकस्प्लैश और बाथरूम हाइलाइटर की दीवारों को विंटेज और मॉडर्न लुक देती हैं।",
            origin = "Morbi, Gujarat",
            previewHex = 0xFF546E7A, // Moroccan blue stone
            drawableRes = null,
            isFeatured = false
        ),
        Product(
            id = "heavy_duty_outdoor_paver",
            name = "Heavy-Duty Full Body Pavers 400x400 mm",
            nameHi = "हैवी ड्यूटी फुल बॉडी पेवर टाइल्स",
            category = ProductCategory.TILES,
            pricePerSqFt = 58.0,
            availableSizes = listOf("400x400 mm (16x16 inch)", "300x300 mm"),
            thickness = "16 mm (Full Body Vitrified)",
            finish = StoneFinish.NATURAL,
            uses = listOf(StoneUse.OUTDOOR, StoneUse.FLOORING),
            descriptionEn = "16mm ultra-thick full-body vitrified pavers engineered for driveways, ramps, open terraces, and car parking. High breaking strength (4000+ N) with punch texture R11 anti-skid rating.",
            descriptionHi = "16 मिमी मोटी फुल बॉडी टाइल्स। कारों की पार्किंग, ड्राइववे और छत के लिए विशेष रूप से निर्मित। कभी रंग नहीं उतरता और गीले मौसम में भी स्लिप नहीं होती।",
            origin = "Morbi, Gujarat",
            previewHex = 0xFF616161, // Charcoal textured paver
            drawableRes = null,
            isFeatured = false
        )
    )

    // Quick project showcase items for Gallery
    data class ProjectGalleryItem(
        val titleEn: String,
        val titleHi: String,
        val category: String,
        val descriptionEn: String,
        val descriptionHi: String,
        val stoneUsed: String,
        val drawableRes: Int
    )

    val GALLERY_PROJECTS: List<ProjectGalleryItem> = listOf(
        ProjectGalleryItem(
            titleEn = "Luxury Villa Living Room & Foyer",
            titleHi = "विला लिविंग रूम और मुख्य हॉल",
            category = "Flooring & Interior",
            descriptionEn = "Seamless book-matched Makrana marble flooring complemented with Black Galaxy border inlays.",
            descriptionHi = "मकराना सफेद मार्बल और ब्लैक गैलेक्सी ग्रेनाइट बॉर्डर का शाही संगम।",
            stoneUsed = "Makrana White Marble & Black Galaxy Granite",
            drawableRes = R.drawable.hero_stone_banner
        ),
        ProjectGalleryItem(
            titleEn = "Heritage Veranda & Garden Patio",
            titleHi = "विशाल बरामदा और गार्डन पाथवे",
            category = "Outdoor Paving",
            descriptionEn = "Hand-cut Kota Blue natural stone paving with chisel dressed edges. Cool barefoot comfort throughout summer.",
            descriptionHi = "नेचुरल कोटा ब्लू स्टोन से तैयार खुला बरामदा, जो गर्मियों में भी ठंडा रहता है।",
            stoneUsed = "Kota Blue Natural Cleft Stone",
            drawableRes = R.drawable.gallery_outdoor_patio
        ),
        ProjectGalleryItem(
            titleEn = "Modular Chef's Kitchen Counter",
            titleHi = "मॉड्यूलर किचन काउंटरटॉप और बैकस्प्लैश",
            category = "Kitchen",
            descriptionEn = "Double-edge rounded Black Galaxy granite platform paired with Moroccan digital feature tiles.",
            descriptionHi = "ब्लैक गैलेक्सी ग्रेनाइट और मोरक्कन डिजाइनर टाइल्स का आधुनिक कॉम्बिनेशन।",
            stoneUsed = "Black Galaxy Granite (20mm)",
            drawableRes = R.drawable.hero_stone_banner
        )
    )
}
