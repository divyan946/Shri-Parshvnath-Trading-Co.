package com.example

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun `room area calculation with 10 percent wastage is correct`() {
        val length = 15.0
        val width = 12.0
        val baseArea = length * width // 180.0
        val withWastage = baseArea * 1.10 // 198.0

        assertEquals(180.0, baseArea, 0.01)
        assertEquals(198.0, withWastage, 0.01)
    }
}
