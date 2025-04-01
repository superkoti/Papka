package com.example.papka.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Светлая цветовая схема
private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),        // Фиолетовый
    onPrimary = Color(0xFFFFFFFF),      // Белый
    secondary = Color(0xFF03DAC6),      // Бирюзовый
    onSecondary = Color(0xFF000000),    // Черный
    background = Color(0xFFF5F5F5),     // Светло-серый
    onBackground = Color(0xFF000000),   // Черный
    surface = Color(0xFFFFFFFF),        // Белый
    onSurface = Color(0xFF000000),      // Черный
    error = Color(0xFFB00020),          // Красный (для ошибок)
    onError = Color(0xFFFFFFFF)         // Белый
)

// Темная цветовая схема
private val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),        // Светло-фиолетовый
    onPrimary = Color(0xFF000000),      // Черный
    secondary = Color(0xFF03DAC6),      // Бирюзовый
    onSecondary = Color(0xFF000000),    // Черный
    background = Color(0xFF121212),     // Темно-серый
    onBackground = Color(0xFFFFFFFF),   // Белый
    surface = Color(0xFF1E1E1E),        // Темно-серый
    onSurface = Color(0xFFFFFFFF),      // Белый
    error = Color(0xFFCF6679),          // Светло-красный
    onError = Color(0xFF000000)         // Черный
)

@Composable
fun PapkaTheme(
    darkTheme: Boolean = false, // Укажите true, чтобы использовать темную тему
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}