package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiDescriptionState
import com.example.ai.AiStoneRepository
import com.example.data.AuthRepository
import com.example.data.ProductCatalog
import com.example.data.QuoteRepository
import com.example.model.AppLanguage
import com.example.model.CustomerEnquiry
import com.example.model.CustomerRole
import com.example.model.OtpStep
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.QuoteItem
import com.example.model.StoneFinish
import com.example.model.StoneUse
import com.example.model.UserAccount
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    CATALOG,
    CALCULATOR,
    QUOTE,
    ABOUT,
    ACCOUNT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val quoteRepository = QuoteRepository(application)
    private val authRepository = AuthRepository(application)
    private val aiRepository = AiStoneRepository()

    // Authenticated User State
    val currentUser: StateFlow<UserAccount> = authRepository.currentUser

    // OTP Flow states
    private val _otpStep = MutableStateFlow<OtpStep>(OtpStep.EnterPhone)
    val otpStep: StateFlow<OtpStep> = _otpStep.asStateFlow()

    private val _otpError = MutableStateFlow<String?>(null)
    val otpError: StateFlow<String?> = _otpError.asStateFlow()

    private val _resendCountdown = MutableStateFlow(30)
    val resendCountdown: StateFlow<Int> = _resendCountdown.asStateFlow()

    private var countdownJob: Job? = null

    // Active Bottom Navigation Tab
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Language Toggle (English / Hindi)
    private val _language = MutableStateFlow(AppLanguage.EN)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    // Master Product Catalog
    val allProducts: List<Product> = ProductCatalog.SAMPLE_PRODUCTS

    // Catalog Filter States
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()

    private val _selectedFinish = MutableStateFlow(StoneFinish.ALL)
    val selectedFinish: StateFlow<StoneFinish> = _selectedFinish.asStateFlow()

    private val _selectedUse = MutableStateFlow(StoneUse.ALL)
    val selectedUse: StateFlow<StoneUse> = _selectedUse.asStateFlow()

    // Filtered Products State
    val filteredProducts: StateFlow<List<Product>> = combine(
        _searchQuery,
        _selectedCategory,
        _selectedFinish,
        _selectedUse
    ) { query, category, finish, use ->
        allProducts.filter { product ->
            val matchesCategory = category == null || product.category == category
            val matchesFinish = finish == StoneFinish.ALL || product.finish == finish
            val matchesUse = use == StoneUse.ALL || product.uses.contains(use)
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.nameHi.contains(query, ignoreCase = true) ||
                    product.descriptionEn.contains(query, ignoreCase = true) ||
                    product.origin.contains(query, ignoreCase = true) ||
                    product.category.displayNameEn.contains(query, ignoreCase = true)

            matchesCategory && matchesFinish && matchesUse && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allProducts)

    // Product Detail Sheet / Modal State
    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail: StateFlow<Product?> = _selectedProductForDetail.asStateFlow()

    // Persistent Quote List
    val quoteItems: StateFlow<List<QuoteItem>> = quoteRepository.quoteItems

    // Persistent Customer Details
    val customerEnquiry: StateFlow<CustomerEnquiry> = quoteRepository.customerEnquiry

    // Area Calculator State
    private val _roomLength = MutableStateFlow("15")
    val roomLength: StateFlow<String> = _roomLength.asStateFlow()

    private val _roomWidth = MutableStateFlow("12")
    val roomWidth: StateFlow<String> = _roomWidth.asStateFlow()

    private val _includeWastage = MutableStateFlow(true)
    val includeWastage: StateFlow<Boolean> = _includeWastage.asStateFlow()

    val calculatedArea: StateFlow<Double> = combine(_roomLength, _roomWidth) { l, w ->
        val length = l.toDoubleOrNull() ?: 0.0
        val width = w.toDoubleOrNull() ?: 0.0
        length * width
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 180.0)

    val suggestedQuantity: StateFlow<Double> = combine(calculatedArea, _includeWastage) { area, wastage ->
        if (wastage) area * 1.10 else area
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 198.0)

    // AI Description State
    private val _aiDescriptionState = MutableStateFlow<AiDescriptionState>(AiDescriptionState.Idle)
    val aiDescriptionState: StateFlow<AiDescriptionState> = _aiDescriptionState.asStateFlow()

    // Navigation and Tab controls
    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.EN) AppLanguage.HI else AppLanguage.EN
    }

    // Catalog Filter Controls
    fun setCategory(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFinish(finish: StoneFinish) {
        _selectedFinish.value = finish
    }

    fun setUse(use: StoneUse) {
        _selectedUse.value = use
    }

    fun openProductDetail(product: Product) {
        _selectedProductForDetail.value = product
        _aiDescriptionState.value = AiDescriptionState.Idle
    }

    fun closeProductDetail() {
        _selectedProductForDetail.value = null
        _aiDescriptionState.value = AiDescriptionState.Idle
    }

    // Quote Cart Operations
    fun addToQuote(product: Product, size: String, quantitySqFt: Double) {
        quoteRepository.addItem(
            productId = product.id,
            productName = product.name,
            category = product.category,
            selectedSize = size,
            quantitySqFt = quantitySqFt,
            pricePerSqFt = product.pricePerSqFt
        )
    }

    fun updateQuoteQuantity(itemId: String, quantitySqFt: Double) {
        quoteRepository.updateQuantity(itemId, quantitySqFt)
    }

    fun removeFromQuote(itemId: String) {
        quoteRepository.removeItem(itemId)
    }

    fun clearQuote() {
        quoteRepository.clearQuote()
    }

    fun saveCustomerEnquiry(enquiry: CustomerEnquiry) {
        quoteRepository.saveCustomerEnquiry(enquiry)
    }

    // Room Calculator Operations
    fun setRoomLength(length: String) {
        _roomLength.value = length
    }

    fun setRoomWidth(width: String) {
        _roomWidth.value = width
    }

    fun setIncludeWastage(include: Boolean) {
        _includeWastage.value = include
    }

    // AI Generation
    fun generateAiDescription(product: Product) {
        viewModelScope.launch {
            _aiDescriptionState.value = AiDescriptionState.Loading
            try {
                val description = aiRepository.generateStoneDescription(product, _language.value)
                _aiDescriptionState.value = AiDescriptionState.Success(description)
            } catch (e: TimeoutCancellationException) {
                Log.e("MainViewModel", "AI Generation timed out", e)
                _aiDescriptionState.value = AiDescriptionState.Error("Gemini AI request timed out. Please check your internet connection and try again.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("MainViewModel", "AI Generation failed", e)
                _aiDescriptionState.value = AiDescriptionState.Error(
                    e.localizedMessage ?: "Failed to generate AI description. Please try again."
                )
            }
        }
    }

    // Authentication & OTP Methods
    fun startOtpFlow() {
        _otpStep.value = OtpStep.EnterPhone
        _otpError.value = null
    }

    fun requestOtp(phone: String, fullName: String, role: CustomerRole, city: String) {
        val cleanPhone = phone.trim().replace(" ", "").replace("-", "")
        if (cleanPhone.length < 10) {
            _otpError.value = "Please enter a valid 10-digit mobile number"
            return
        }
        _otpError.value = null
        val generatedCode = authRepository.generateOtp(cleanPhone)
        _resendCountdown.value = 30
        _otpStep.value = OtpStep.EnterOtp(
            phone = cleanPhone,
            generatedOtp = generatedCode,
            resendCountdownSeconds = 30
        )
        startCountdownTimer()
    }

    fun resendOtp(phone: String) {
        if (_resendCountdown.value > 0) return
        val generatedCode = authRepository.generateOtp(phone)
        _otpError.value = null
        _resendCountdown.value = 30
        _otpStep.value = OtpStep.EnterOtp(
            phone = phone,
            generatedOtp = generatedCode,
            resendCountdownSeconds = 30
        )
        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_resendCountdown.value > 0) {
                delay(1000)
                _resendCountdown.value = _resendCountdown.value - 1
            }
        }
    }

    fun verifyAndLogin(
        phone: String,
        enteredOtp: String,
        fullName: String,
        role: CustomerRole,
        city: String,
        gstin: String = ""
    ): Boolean {
        val isValid = authRepository.verifyOtp(phone, enteredOtp)
        if (!isValid) {
            _otpError.value = "Invalid OTP code. Please enter the 6-digit code shown or '123456'."
            return false
        }
        _otpError.value = null
        countdownJob?.cancel()
        val account = authRepository.login(
            phoneNumber = phone,
            fullName = if (fullName.isBlank()) "Stone Buyer" else fullName,
            role = role,
            deliveryCity = city,
            gstin = gstin
        )
        _otpStep.value = OtpStep.Verified(account)

        // Automatically prefill customer enquiry so quote & payment flow are populated
        val currentEnquiry = customerEnquiry.value
        quoteRepository.saveCustomerEnquiry(
            currentEnquiry.copy(
                name = account.fullName,
                phone = account.phoneNumber,
                address = if (account.deliveryCity.isNotBlank()) account.deliveryCity else currentEnquiry.address,
                projectType = account.role.titleEn
            )
        )
        return true
    }

    fun logout() {
        countdownJob?.cancel()
        authRepository.logout()
        _otpStep.value = OtpStep.EnterPhone
        _otpError.value = null
    }

    fun updateProfile(fullName: String, role: CustomerRole, city: String, gstin: String) {
        authRepository.updateProfile(fullName, role, city, gstin)
    }
}

