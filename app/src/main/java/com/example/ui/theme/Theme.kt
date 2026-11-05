package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccentLight,
    onPrimary = DarkBackground,
    primaryContainer = StoneBrownDark,
    onPrimaryContainer = GoldContainer,
    secondary = DarkStoneBrown,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = WarmStoneLight,
    tertiary = GoldAccent,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = Color(0xFFF1EFEA),
    surface = DarkSurface,
    onSurface = Color(0xFFF1EFEA),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCDC6BD),
    outline = Color(0xFF5D534A)
)

private val LightColorScheme = lightColorScheme(
    primary = StoneBrownPrimary,
    onPrimary = PureWhite,
    primaryContainer = StoneBrownLight,
    onPrimaryContainer = PureWhite,
    secondary = WarmStoneGrey,
    onSecondary = PureWhite,
    secondaryContainer = WarmStoneLight,
    onSecondaryContainer = StoneBrownDark,
    tertiary = GoldAccent,
    onTertiary = PureWhite,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = OnGoldContainer,
    background = WarmStoneBackground,
    onBackground = Color(0xFF221F1C),
    surface = PureWhite,
    onSurface = Color(0xFF221F1C),
    surfaceVariant = WarmStoneLight,
    onSurfaceVariant = Color(0xFF4C453E),
    outline = Color(0xFFD3CCC3)
)

@Composable
fun ParshvnathTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
