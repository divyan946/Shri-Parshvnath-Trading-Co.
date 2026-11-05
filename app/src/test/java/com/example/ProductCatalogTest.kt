package com.example

import com.example.data.ProductCatalog
import com.example.model.ProductCategory
import com.example.model.QuoteItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProductCatalogTest {

    @Test
    fun verifyCatalogHasTwelveProductsWithThreePerCategory() {
        val products = ProductCatalog.SAMPLE_PRODUCTS
        assertEquals(12, products.size)

        val kotaStones = products.filter { it.category == ProductCategory.KOTA_STONE }
        assertEquals(3, kotaStones.size)

        val marbles = products.filter { it.category == ProductCategory.MARBLE }
        assertEquals(3, marbles.size)

        val granites = products.filter { it.category == ProductCategory.GRANITE }
        assertEquals(3, granites.size)

        val tiles = products.filter { it.category == ProductCategory.TILES }
        assertEquals(3, tiles.size)

        products.forEach { product ->
            assertNotNull(product.name)
            assertNotNull(product.nameHi)
            assertNotNull(product.thickness)
            assertNotNull(product.finish)
        }
    }

    @Test
    fun verifyQuoteItemTotalPriceCalculation() {
        val item = QuoteItem(
            id = "test-1",
            productId = "kota_blue_natural",
            productName = "Kota Blue Natural Stone",
            category = ProductCategory.KOTA_STONE,
            selectedSize = "2x2 ft",
            quantitySqFt = 250.0,
            pricePerSqFt = 28.0
        )
        assertEquals(7000.0, item.totalPrice, 0.01)
    }

    @Test
    fun verifyFiftyFiftyPaymentMilestoneCalculation() {
        val totalOrder = 50000.0
        val advance = totalOrder * 0.50
        val balanceDue = totalOrder - advance
        assertEquals(25000.0, advance, 0.01)
        assertEquals(25000.0, balanceDue, 0.01)
    }

    @Test
    fun verifyCardBrandDetectionAndFormatting() {
        assertEquals("Visa", com.example.util.PaymentHelper.detectCardBrand("4111222233334444"))
        assertEquals("Mastercard", com.example.util.PaymentHelper.detectCardBrand("5200111122223333"))
        assertEquals("RuPay", com.example.util.PaymentHelper.detectCardBrand("6071111122223333"))
        assertEquals("4111 2222 3333 4444", com.example.util.PaymentHelper.formatCardNumber("4111222233334444"))
    }

    @Test
    fun verifyInvoiceCredentials() {
        assertEquals("08AIHPJ8128R1ZI", com.example.util.WhatsAppHelper.GSTIN)
        assertEquals("nileshj24679@gmail.com", com.example.util.WhatsAppHelper.BUSINESS_EMAIL)
        assertEquals("+91 94130 85910", com.example.util.WhatsAppHelper.DISPLAY_PHONE)
        assertEquals("+91 92143 85910", com.example.util.WhatsAppHelper.SECONDARY_PHONE)
        org.junit.Assert.assertTrue(com.example.util.WhatsAppHelper.SHOP_ADDRESS.contains("Ramganjmandi"))
        org.junit.Assert.assertTrue(com.example.util.WhatsAppHelper.SHOP_ADDRESS.contains("326519"))
    }

    @Test
    fun verifyUserAccountAndRoles() {
        val user = com.example.model.UserAccount(
            id = "CUST-1234",
            phoneNumber = "9413085910",
            fullName = "Ramesh Kumar",
            role = com.example.model.CustomerRole.CONTRACTOR,
            deliveryCity = "Kota",
            isLoggedIn = true
        )
        org.junit.Assert.assertTrue(user.isLoggedIn)
        assertEquals("9413085910", user.phoneNumber)
        assertEquals(com.example.model.CustomerRole.CONTRACTOR, user.role)
        assertEquals(4, com.example.model.CustomerRole.values().size)
    }
}
