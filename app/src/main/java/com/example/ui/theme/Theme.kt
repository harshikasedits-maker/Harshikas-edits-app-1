package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GoldBright,
    onPrimary = RoyalNavyDark,
    primaryContainer = RoyalNavyCard,
    onPrimaryContainer = GoldLight,
    secondary = GoldAccent,
    onSecondary = RoyalNavyDark,
    secondaryContainer = RoyalNavySurface,
    onSecondaryContainer = GoldLight,
    tertiary = SkyBlueInfo,
    background = RoyalNavyDark,
    onBackground = Color(0xFFF1F5F9),
    surface = RoyalNavySurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = RoyalNavyCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF2E466B)
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalNavyLight,
    onPrimaryContainer = Color.White,
    secondary = GoldAccent,
    onSecondary = RoyalNavyDark,
    secondaryContainer = GoldLight,
    onSecondaryContainer = RoyalNavyDark,
    tertiary = SkyBlueInfo,
    background = OffWhiteBg,
    onBackground = TextPrimaryDark,
    surface = CardLightBg,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryDark,
    outline = SlateBorder
)

@Composable
fun HarshikasEditsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded royal navy and gold theme by default
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Alias for backwards compatibility with any template references
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    HarshikasEditsTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
