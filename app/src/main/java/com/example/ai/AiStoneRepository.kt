package com.example.ai

import android.util.Log
import com.example.model.AppLanguage
import com.example.model.Product
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

private const val TAG = "AiStoneRepository"

sealed interface AiDescriptionState {
    data object Idle : AiDescriptionState
    data object Loading : AiDescriptionState
    data class Success(val generatedText: String) : AiDescriptionState
    data class Error(val message: String) : AiDescriptionState
}

class AiStoneRepository {

    // Lazy initialization of the generative model to avoid startup crashes
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                temperature = 0.7f
                topP = 0.95f
            }
        )
    }

    suspend fun generateStoneDescription(product: Product, language: AppLanguage): String {
        return withTimeout(60_000L) {
            val langInstruction = if (language == AppLanguage.HI) {
                "Generate the response strictly in fluent, professional Hindi (हिंदी). Use clear, respectful language suited for Indian homeowners, architects, and contractors."
            } else {
                "Generate the response in clear, inviting, and professional English suited for premium homeowners, architects, and interior designers."
            }

            val prompt = """
                You are a senior stone consultant and architectural stone specialist at 'Shri Parshvnath Trading Co.', Rajasthan's premier dealer in Kota Stone, Marble, Granite, and Tiles.
                
                Write an appealing, professional product showcase for the following stone product:
                - Name: ${product.name} (${product.nameHi})
                - Category: ${product.category.displayNameEn}
                - Natural Finish: ${product.finish.labelEn}
                - Standard Thickness: ${product.thickness}
                - Rate: ₹${product.pricePerSqFt} per sq ft
                - Known Origin: ${product.origin}
                - Core Features: ${product.descriptionEn}

                Provide a structured response with:
                1. ✨ **Aesthetic & Character**: A vivid 2-3 sentence overview highlighting the stone's natural beauty, texture, and lasting value.
                2. 🏛️ **Where It Is Best Used**: Bullet points for top 3 recommended architectural applications (e.g. living room flooring, exterior verandas, heavy-traffic corridors, kitchen platforms, etc.) explaining why this stone excels there.
                3. 💡 **Masonry & Care Tip**: 1 short practical tip for installation, jointing, or longevity.

                $langInstruction
                Keep the tone trustworthy, elegant, and commercial. Do not include markdown code fences or conversational filler.
            """.trimIndent()

            val response = model.generateContent(prompt)
            val text = response.text
            if (!text.isNullOrBlank()) {
                text.trim()
            } else {
                throw IllegalStateException("Model returned an empty response.")
            }
        }
    }
}
