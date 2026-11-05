package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ProductCategory
import com.example.model.StoneFinish
import com.example.model.StoneUse
import com.example.ui.components.ProductCard
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.StoneBrownPrimary
import com.example.util.AppStrings
import com.example.viewmodel.MainViewModel

@Composable
fun CatalogScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedFinish by viewModel.selectedFinish.collectAsState()
    val selectedUse by viewModel.selectedUse.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()

    val categories = listOf(null) + ProductCategory.entries

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp) // Space for bottom bar
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text(AppStrings.searchPlaceholder(language), fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("catalog_search_bar")
        )

        // Category Tabs
        val selectedTabIndex = categories.indexOf(selectedCategory)
        ScrollableTabRow(
            selectedTabIndex = if (selectedTabIndex >= 0) selectedTabIndex else 0,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                val index = if (selectedTabIndex >= 0) selectedTabIndex else 0
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                    color = StoneBrownPrimary
                )
            },
            divider = {}
        ) {
            categories.forEachIndexed { index, category ->
                val title = if (category == null) {
                    AppStrings.allCategories(language)
                } else {
                    if (language == AppLanguage.HI) category.displayNameHi else category.displayNameEn
                }

                Tab(
                    selected = selectedCategory == category,
                    onClick = { viewModel.setCategory(category) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Finish & Use Filter Rows
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(vertical = 6.dp)
        ) {
            // Finish Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = AppStrings.finishFilter(language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                StoneFinish.entries.forEach { finish ->
                    FilterChip(
                        selected = selectedFinish == finish,
                        onClick = { viewModel.setFinish(finish) },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HI) finish.labelHi else finish.labelEn,
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // Usage Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = AppStrings.useFilter(language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                StoneUse.entries.forEach { use ->
                    FilterChip(
                        selected = selectedUse == use,
                        onClick = { viewModel.setUse(use) },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HI) use.labelHi else use.labelEn,
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Product Count Summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.HI) "${filteredProducts.size} उत्पाद उपलब्ध" else "Showing ${filteredProducts.size} Products",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (searchQuery.isNotEmpty() || selectedFinish != StoneFinish.ALL || selectedUse != StoneUse.ALL || selectedCategory != null) {
                Text(
                    text = if (language == AppLanguage.HI) "फ़िल्टर हटाएं" else "Reset Filters",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StoneBrownPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // Product Grid
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.HI) "कोई उत्पाद नहीं मिला" else "No matching products found",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.HI) "कृपया अपनी खोज या फ़िल्टर बदलें" else "Try adjusting your search terms or filter selection",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            viewModel.setSearchQuery("")
                            viewModel.setCategory(null)
                            viewModel.setFinish(StoneFinish.ALL)
                            viewModel.setUse(StoneUse.ALL)
                        }
                    ) {
                        Text(if (language == AppLanguage.HI) "सभी उत्पाद देखें" else "View All Products")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("catalog_grid")
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        language = language,
                        onProductClick = { viewModel.openProductDetail(product) },
                        onAddToQuoteClick = {
                            viewModel.addToQuote(
                                product = product,
                                size = product.availableSizes.firstOrNull() ?: "Standard",
                                quantitySqFt = 100.0
                            )
                        }
                    )
                }
            }
        }
    }
}
