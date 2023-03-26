package com.bizarre.assistedreminderapp.ui.theme

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bizarre.assistedreminderapp.AssistedReminderApp


private val DarkColorPalette = darkColors(
    primary = Gray100,
    primaryVariant = Gray100,
    secondary = Gray200,
    background  = Color.Transparent
)

private val LightColorPalette = lightColors(
    primary = Gray200,
    primaryVariant = Gray200,
    secondary = Gray100,
    background  = Color.Transparent


)

@Composable
fun AssistedReminderAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }



    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

