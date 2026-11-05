package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.StoneBrownPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.AppStrings
import com.example.util.WhatsAppHelper
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenTab

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalculatorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val roomLength by viewModel.roomLength.collectAsState()
    val roomWidth by viewModel.roomWidth.collectAsState()
    val includeWastage by viewModel.includeWastage.collectAsState()
    val calculatedArea by viewModel.calculatedArea.collectAsState()
    val suggestedQuantity by viewModel.suggestedQuantity.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = AppStrings.calculatorTitle(language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = AppStrings.calculatorSubtitle(language),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Inputs Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.HI) "कमरे का माप दर्ज करें (फीट में)" else "Enter Room Dimensions (in Feet)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = roomLength,
                        onValueChange = { viewModel.setRoomLength(it.filter { c -> c.isDigit() || c == '.' }) },
                        label = { Text(if (language == AppLanguage.HI) "लंबाई (फीट)" else "Length (ft)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("calculator_length_input")
                    )

                    OutlinedTextField(
                        value = roomWidth,
                        onValueChange = { viewModel.setRoomWidth(it.filter { c -> c.isDigit() || c == '.' }) },
                        label = { Text(if (language == AppLanguage.HI) "चौड़ाई (फीट)" else "Width (ft)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("calculator_width_input")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Presets
                Text(
                    text = if (language == AppLanguage.HI) "सामान्य कमरे के आकार:" else "Quick Preset Sizes:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(
                        "Living Hall" to Pair("20", "16"),
                        "Master Bed" to Pair("15", "13"),
                        "Kitchen" to Pair("12", "10"),
                        "Veranda" to Pair("24", "12")
                    )
                    presets.forEach { (label, dims) ->
                        FilterChip(
                            selected = roomLength == dims.first && roomWidth == dims.second,
                            onClick = {
                                viewModel.setRoomLength(dims.first)
                                viewModel.setRoomWidth(dims.second)
                            },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Wastage Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.HI) "+10% वेस्टेज शामिल करें" else "Include +10% Cutting Wastage",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (language == AppLanguage.HI) "कोने की कटिंग व स्कर्टिंग के लिए आवश्यक" else "Recommended for corners, skirting & cuts",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = includeWastage,
                        onCheckedChange = { viewModel.setIncludeWastage(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = StoneBrownPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Calculation Results Card
        OutlinedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            border = BorderStroke(1.5.dp, GoldAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.HI) "वास्तविक क्षेत्रफल (फ्लोर एरिया)" else "Net Room Area",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${calculatedArea.toInt()} sq ft",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (language == AppLanguage.HI) "अनुशंसित ऑर्डर मात्रा" else "Suggested Order Quantity",
                            fontSize = 12.sp,
                            color = StoneBrownPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${suggestedQuantity.toInt()} sq ft",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StoneBrownPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.HI) "10% अतिरिक्त जोड़ने से बाद में टाइल्स या पत्थर का लॉट मिसमैच नहीं होता।" else "Adding 10% avoids future color/lot mismatch during masonry installation.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Next Action Buttons
        Button(
            onClick = { viewModel.selectTab(ScreenTab.CATALOG) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
            Text(
                text = if (language == AppLanguage.HI) "इस मात्रा के लिए पत्थर चुनें (${suggestedQuantity.toInt()} sq ft)" else "Browse Stones for ${suggestedQuantity.toInt()} sq ft",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val text = "Hello Shri Parshvnath Trading Co., my room size is $roomLength ft x $roomWidth ft (Total area: ${suggestedQuantity.toInt()} sq ft with 10% wastage). Please suggest suitable Kota stone / marble / granite options and pricing."
                WhatsAppHelper.sendDirectEnquiry(context, text)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (language == AppLanguage.HI) "इस माप के साथ व्हाट्सएप पर सलाह लें" else "Consult on WhatsApp for this Area",
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
