package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.ui.theme.GoldAccent

@Composable
fun StoneTextureBox(
    product: Product,
    modifier: Modifier = Modifier,
    showBadge: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(product.previewHex))
    ) {
        if (product.drawableRes != null) {
            Image(
                painter = painterResource(id = product.drawableRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Subtle gradient overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x66000000))
                        )
                    )
            )
        } else {
            // Procedural natural stone vein pattern
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val baseColor = Color(product.previewHex)
                        // Diagonal stone strata lines
                        val veinColor = if (baseColor.red > 0.6f && baseColor.green > 0.6f && baseColor.blue > 0.6f) {
                            Color(0x22808080) // Darker veins for light marble
                        } else {
                            Color(0x26FFFFFF) // Lighter crystalline flecks for dark stone
                        }

                        drawLine(
                            color = veinColor,
                            start = Offset(0f, size.height * 0.25f),
                            end = Offset(size.width, size.height * 0.75f),
                            strokeWidth = 3f
                        )
                        drawLine(
                            color = veinColor,
                            start = Offset(size.width * 0.2f, 0f),
                            end = Offset(size.width * 0.9f, size.height),
                            strokeWidth = 1.5f
                        )
                        drawLine(
                            color = veinColor,
                            start = Offset(0f, size.height * 0.6f),
                            end = Offset(size.width * 0.7f, size.height),
                            strokeWidth = 2f
                        )
                    }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.12f),
                                Color.Black.copy(alpha = 0.25f)
                            )
                        )
                    )
            )
        }

        if (showBadge) {
            Surface(
                color = Color(0xCC1A1816),
                shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = product.category.displayNameEn,
                    color = GoldAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}
