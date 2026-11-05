package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CustomerRole
import com.example.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AuthRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("parshvnath_auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow(loadInitialUser())
    val currentUser: StateFlow<UserAccount> = _currentUser.asStateFlow()

    // Transient OTP generation state for currently active verification session
    private var activeGeneratedOtp: String = ""
    private var activeOtpPhone: String = ""

    private fun loadInitialUser(): UserAccount {
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        if (!isLoggedIn) {
            return UserAccount(isLoggedIn = false)
        }
        val id = prefs.getString("user_id", "") ?: ""
        val phone = prefs.getString("user_phone", "") ?: ""
        val name = prefs.getString("user_name", "") ?: ""
        val roleStr = prefs.getString("user_role", CustomerRole.HOME_BUILDER.name) ?: CustomerRole.HOME_BUILDER.name
        val role = try {
            CustomerRole.valueOf(roleStr)
        } catch (_: Exception) {
            CustomerRole.HOME_BUILDER
        }
        val city = prefs.getString("user_city", "") ?: ""
        val gstin = prefs.getString("user_gstin", "") ?: ""
        val time = prefs.getLong("login_timestamp", System.currentTimeMillis())

        return UserAccount(
            id = id,
            phoneNumber = phone,
            fullName = name,
            role = role,
            deliveryCity = city,
            gstin = gstin,
            isLoggedIn = true,
            loginTimeMillis = time
        )
    }

    /**
     * Generates a 6-digit OTP code for the requested phone number.
     * Generates a realistic random 6-digit number.
     */
    fun generateOtp(phone: String): String {
        // Generate random 6-digit OTP
        val code = (100000..999999).random().toString()
        activeGeneratedOtp = code
        activeOtpPhone = phone.trim()
        return code
    }

    /**
     * Checks if the entered OTP matches the generated OTP or fallback sandbox code "123456".
     */
    fun verifyOtp(phone: String, enteredOtp: String): Boolean {
        val cleanOtp = enteredOtp.trim()
        val cleanPhone = phone.trim()
        // Sandbox fallback code for developer testing or the freshly generated OTP
        val isValid = cleanOtp == "123456" || (cleanOtp == activeGeneratedOtp && (activeOtpPhone.isEmpty() || activeOtpPhone == cleanPhone))
        return isValid
    }

    /**
     * Logs the user in and persists their session.
     */
    fun login(
        phoneNumber: String,
        fullName: String,
        role: CustomerRole = CustomerRole.HOME_BUILDER,
        deliveryCity: String = "",
        gstin: String = ""
    ): UserAccount {
        val id = prefs.getString("user_id", null) ?: "CUST-${UUID.randomUUID().toString().take(8).uppercase()}"
        val timestamp = System.currentTimeMillis()

        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_id", id)
            .putString("user_phone", phoneNumber.trim())
            .putString("user_name", fullName.trim())
            .putString("user_role", role.name)
            .putString("user_city", deliveryCity.trim())
            .putString("user_gstin", gstin.trim())
            .putLong("login_timestamp", timestamp)
            .apply()

        val account = UserAccount(
            id = id,
            phoneNumber = phoneNumber.trim(),
            fullName = fullName.trim(),
            role = role,
            deliveryCity = deliveryCity.trim(),
            gstin = gstin.trim(),
            isLoggedIn = true,
            loginTimeMillis = timestamp
        )
        _currentUser.value = account
        return account
    }

    /**
     * Updates profile attributes while logged in.
     */
    fun updateProfile(
        fullName: String,
        role: CustomerRole,
        deliveryCity: String,
        gstin: String
    ) {
        val current = _currentUser.value
        if (!current.isLoggedIn) return

        val updated = current.copy(
            fullName = fullName.trim(),
            role = role,
            deliveryCity = deliveryCity.trim(),
            gstin = gstin.trim()
        )

        prefs.edit()
            .putString("user_name", updated.fullName)
            .putString("user_role", updated.role.name)
            .putString("user_city", updated.deliveryCity)
            .putString("user_gstin", updated.gstin)
            .apply()

        _currentUser.value = updated
    }

    /**
     * Logs the user out, clearing the persistent session while preserving general quote cart items.
     */
    fun logout() {
        prefs.edit()
            .putBoolean("is_logged_in", false)
            .remove("login_timestamp")
            .apply()

        _currentUser.value = UserAccount(isLoggedIn = false)
        activeGeneratedOtp = ""
        activeOtpPhone = ""
    }
}
