package com.hashone.cropper.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hashone.cropper.builder.Crop

private val DarkColorScheme = darkColorScheme(
    primary = DefaultOverlayColor,
    secondary = DefaultOverlayColor,
    tertiary = DefaultOverlayColor
)

private val LightColorScheme = lightColorScheme(
    primary = DefaultOverlayColor,
    secondary = DefaultOverlayColor,
    tertiary = DefaultOverlayColor

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ComposeCropperTheme(
    cropBuilder: Crop.Builder,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val systemUiController = rememberSystemUiController()
//        SideEffect {
//            systemUiController.setSystemBarsColor(
//                DefaultHandleColor,
//                darkIcons = true)
//            systemUiController.setStatusBarColor(
//                DefaultHandleColor,
//                darkIcons = true
//            )
//            systemUiController.setNavigationBarColor(
//                DefaultHandleColor,
//                darkIcons = true
//            )
//        }
    }

    MaterialTheme(
        typography = Typography,
        content = content
    )
}
