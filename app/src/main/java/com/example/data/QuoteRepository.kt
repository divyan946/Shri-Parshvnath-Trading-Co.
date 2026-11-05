package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CustomerEnquiry
import com.example.model.ProductCategory
import com.example.model.QuoteItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class QuoteRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parshvnath_quote_prefs", Context.MODE_PRIVATE)

    private val _quoteItems = MutableStateFlow<List<QuoteItem>>(emptyList())
    val quoteItems: StateFlow<List<QuoteItem>> = _quoteItems.asStateFlow()

    private val _customerEnquiry = MutableStateFlow(CustomerEnquiry())
    val customerEnquiry: StateFlow<CustomerEnquiry> = _customerEnquiry.asStateFlow()

    init {
        loadSavedQuote()
        loadSavedEnquiry()
    }

    private fun loadSavedQuote() {
        val jsonString = prefs.getString("saved_quote_items", null) ?: return
        try {
            val array = JSONArray(jsonString)
            val list = mutableListOf<QuoteItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    QuoteItem(
                        id = obj.getString("id"),
                        productId = obj.getString("productId"),
                        productName = obj.getString("productName"),
                        category = ProductCategory.valueOf(obj.getString("category")),
                        selectedSize = obj.getString("selectedSize"),
                        quantitySqFt = obj.getDouble("quantitySqFt"),
                        pricePerSqFt = obj.getDouble("pricePerSqFt")
                    )
                )
            }
            _quoteItems.value = list
        } catch (_: Exception) {
            _quoteItems.value = emptyList()
        }
    }

    private fun saveQuote() {
        try {
            val array = JSONArray()
            _quoteItems.value.forEach { item ->
                val obj = JSONObject().apply {
                    put("id", item.id)
                    put("productId", item.productId)
                    put("productName", item.productName)
                    put("category", item.category.name)
                    put("selectedSize", item.selectedSize)
                    put("quantitySqFt", item.quantitySqFt)
                    put("pricePerSqFt", item.pricePerSqFt)
                }
                array.put(obj)
            }
            prefs.edit().putString("saved_quote_items", array.toString()).apply()
        } catch (_: Exception) {
        }
    }

    fun addItem(
        productId: String,
        productName: String,
        category: ProductCategory,
        selectedSize: String,
        quantitySqFt: Double,
        pricePerSqFt: Double
    ) {
        val current = _quoteItems.value.toMutableList()
        // If exact product and size already exists, aggregate quantity
        val existingIndex = current.indexOfFirst { it.productId == productId && it.selectedSize == selectedSize }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(
                quantitySqFt = existing.quantitySqFt + quantitySqFt
            )
        } else {
            current.add(
                QuoteItem(
                    id = java.util.UUID.randomUUID().toString(),
                    productId = productId,
                    productName = productName,
                    category = category,
                    selectedSize = selectedSize,
                    quantitySqFt = quantitySqFt,
                    pricePerSqFt = pricePerSqFt
                )
            )
        }
        _quoteItems.value = current
        saveQuote()
    }

    fun updateQuantity(itemId: String, newQuantity: Double) {
        if (newQuantity <= 0) {
            removeItem(itemId)
            return
        }
        val current = _quoteItems.value.toMutableList()
        val index = current.indexOfFirst { it.id == itemId }
        if (index >= 0) {
            current[index] = current[index].copy(quantitySqFt = newQuantity)
            _quoteItems.value = current
            saveQuote()
        }
    }

    fun removeItem(itemId: String) {
        _quoteItems.value = _quoteItems.value.filterNot { it.id == itemId }
        saveQuote()
    }

    fun clearQuote() {
        _quoteItems.value = emptyList()
        prefs.edit().remove("saved_quote_items").apply()
    }

    private fun loadSavedEnquiry() {
        val name = prefs.getString("enquiry_name", "") ?: ""
        val phone = prefs.getString("enquiry_phone", "") ?: ""
        val address = prefs.getString("enquiry_address", "") ?: ""
        val type = prefs.getString("enquiry_type", "Home / Villa") ?: "Home / Villa"
        val notes = prefs.getString("enquiry_notes", "") ?: ""
        _customerEnquiry.value = CustomerEnquiry(name, phone, address, type, notes)
    }

    fun saveCustomerEnquiry(enquiry: CustomerEnquiry) {
        _customerEnquiry.value = enquiry
        prefs.edit()
            .putString("enquiry_name", enquiry.name)
            .putString("enquiry_phone", enquiry.phone)
            .putString("enquiry_address", enquiry.address)
            .putString("enquiry_type", enquiry.projectType)
            .putString("enquiry_notes", enquiry.notes)
            .apply()
    }
}
