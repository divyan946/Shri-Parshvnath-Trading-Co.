package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.ui.components.FloatingWhatsAppButton
import com.example.ui.screens.AboutContactScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProductDetailDialog
import com.example.ui.screens.QuoteScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.ParshvnathTheme
import com.example.ui.theme.StoneBrownDark
import com.example.ui.theme.StoneBrownPrimary
import com.example.util.AppStrings
import com.example.util.configureAppCheck
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check initialization with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            ParshvnathTheme {
                val viewModel: MainViewModel = viewModel()
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val language by viewModel.language.collectAsState()
    val quoteItems by viewModel.quoteItems.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val selectedProduct by viewModel.selectedProductForDetail.collectAsState()

    // Handle Android system back press: if not on Home screen, navigate to Home
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        viewModel.selectTab(ScreenTab.HOME)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.selectTab(ScreenTab.HOME) }
                    ) {
                        Surface(
                            color = GoldContainer,
                            shape = CircleShape,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "SP",
                                    color = StoneBrownDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = AppStrings.appTitle(language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1
                            )
                            Text(
                                text = AppStrings.appSubtitle(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                },
                actions = {
                    // English / Hindi Language Toggle Button
                    OutlinedButton(
                        onClick = { viewModel.toggleLanguage() },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, GoldAccent),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .testTag("language_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = GoldAccent,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.EN) "हिन्दी" else "English",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Account / Login Button
                    if (currentUser.isLoggedIn) {
                        Surface(
                            shape = CircleShape,
                            color = if (currentTab == ScreenTab.ACCOUNT) GoldContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.5.dp, if (currentTab == ScreenTab.ACCOUNT) StoneBrownPrimary else GoldAccent),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(36.dp)
                                .clickable { viewModel.selectTab(ScreenTab.ACCOUNT) }
                                .testTag("top_bar_account_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val initial = currentUser.fullName.trim().take(1).uppercase().ifEmpty { "U" }
                                Text(
                                    text = initial,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = StoneBrownDark
                                )
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.selectTab(ScreenTab.ACCOUNT) },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, if (currentTab == ScreenTab.ACCOUNT) StoneBrownPrimary else GoldAccent),
                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                containerColor = if (currentTab == ScreenTab.ACCOUNT) GoldContainer else Color.Transparent
                            ),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .testTag("top_bar_login_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Login",
                                tint = StoneBrownPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.EN) "Login" else "लॉगिन",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StoneBrownPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                // Home Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.HOME,
                    onClick = { viewModel.selectTab(ScreenTab.HOME) },
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(AppStrings.navHome(language), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StoneBrownPrimary,
                        selectedTextColor = StoneBrownPrimary,
                        indicatorColor = GoldContainer
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                // Catalog Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.CATALOG,
                    onClick = { viewModel.selectTab(ScreenTab.CATALOG) },
                    icon = { Icon(imageVector = Icons.Default.GridView, contentDescription = "Catalog") },
                    label = { Text(AppStrings.navCatalog(language), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StoneBrownPrimary,
                        selectedTextColor = StoneBrownPrimary,
                        indicatorColor = GoldContainer
                    ),
                    modifier = Modifier.testTag("nav_catalog")
                )

                // Calculator Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.CALCULATOR,
                    onClick = { viewModel.selectTab(ScreenTab.CALCULATOR) },
                    icon = { Icon(imageVector = Icons.Default.Calculate, contentDescription = "Calculator") },
                    label = { Text(AppStrings.navCalculator(language), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StoneBrownPrimary,
                        selectedTextColor = StoneBrownPrimary,
                        indicatorColor = GoldContainer
                    ),
                    modifier = Modifier.testTag("nav_calculator")
                )

                // Quote / Cart Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.QUOTE,
                    onClick = { viewModel.selectTab(ScreenTab.QUOTE) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (quoteItems.isNotEmpty()) {
                                    Badge(
                                        containerColor = Color(0xFFD32F2F),
                                        contentColor = Color.White
                                    ) {
                                        Text("${quoteItems.size}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Quote")
                        }
                    },
                    label = { Text(AppStrings.navQuote(language), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StoneBrownPrimary,
                        selectedTextColor = StoneBrownPrimary,
                        indicatorColor = GoldContainer
                    ),
                    modifier = Modifier.testTag("nav_quote")
                )

                // About & Contact Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.ABOUT,
                    onClick = { viewModel.selectTab(ScreenTab.ABOUT) },
                    icon = { Icon(imageVector = Icons.Default.Storefront, contentDescription = "About") },
                    label = { Text(AppStrings.navAbout(language), fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StoneBrownPrimary,
                        selectedTextColor = StoneBrownPrimary,
                        indicatorColor = GoldContainer
                    ),
                    modifier = Modifier.testTag("nav_about")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Content Area with transition animation
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "TabContentTransition"
            ) { targetTab ->
                when (targetTab) {
                    ScreenTab.HOME -> HomeScreen(viewModel = viewModel)
                    ScreenTab.CATALOG -> CatalogScreen(viewModel = viewModel)
                    ScreenTab.CALCULATOR -> CalculatorScreen(viewModel = viewModel)
                    ScreenTab.QUOTE -> QuoteScreen(viewModel = viewModel)
                    ScreenTab.ABOUT -> AboutContactScreen(viewModel = viewModel)
                    ScreenTab.ACCOUNT -> AuthScreen(viewModel = viewModel)
                }
            }

            // Floating WhatsApp Button on Every Page (positioned in bottom-end corner)
            FloatingWhatsAppButton(
                quoteCount = quoteItems.size,
                language = language,
                onQuoteClick = { viewModel.selectTab(ScreenTab.QUOTE) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )

            // Product Detail Dialog
            if (selectedProduct != null) {
                ProductDetailDialog(
                    product = selectedProduct!!,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeProductDetail() }
                )
            }
        }
    }
}
