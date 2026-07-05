package com.yaeldev.cursitodefundamentosandroid.core.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val primary = Color(0XFF4361ee) // FAB, active nav, primary buttons, header title
val primaryContainer = Color(0xFFeef0ff) // Nav pill, avatar action buttons, search bar fill
val background = Color(0xFFf8f9fe) // App background
val surface = Color(0xFFffffff) // Cards, app bars, list rows, bottom nav
val onSurface = Color(0xFF1a1c2e) // Primary text, screen titles
val onSurfaceVariant = Color(0xFF9098b1) // Secondary text, placeholders, inactive icons
val outline = Color(0xFFf0f2ff) // Dividers between rows/fields
val outlineVariant = Color(0xFFeef0ff) // Section borders, containers
val error = Color(0xFFdc2626) // Validation errors, delete actions
val errorContainer = Color(0xFFfef2f2) // Delete button background

val red = Color(0xFFe53935)
val pink = Color(0xFFd81b60)
val purple = Color(0xFF8e24aa)
val deepPurple = Color(0xFF5e35b1)
val indigo = Color(0xFF3949ab)
val blue = Color(0xFF1e88e5)

/** Paleta para avatares de contactos. */
val avatarColores = listOf(red, pink, purple, deepPurple, indigo, blue)

/**
 * Color de avatar derivado de [clave] (p. ej. el nombre): mismo contacto -> mismo
 * color siempre (no aleatorio por recomposicion), pero la lista se ve variada.
 */
fun colorAvatar(clave: String): Color {
    if (clave.isBlank()) return avatarColores.first()
    val indice = ((clave.hashCode() % avatarColores.size) + avatarColores.size) % avatarColores.size
    return avatarColores[indice]
}

// Modo oscuro: los fondos/superficies se derivan del color claro equivalente
// escalado al 20% de brillo (cada canal RGB * 0.2). El primary/error de marca se
// reutilizan y los textos se mantienen claros para conservar contraste.
val backgroundDark = Color(0xFF323233)       // background 0xf8f9fe * 0.2
val surfaceDark = Color(0xFF333333)          // surface 0xffffff * 0.2
val primaryContainerDark = Color(0xFF303033) // primaryContainer 0xeef0ff * 0.2
val outlineDark = Color(0xFF303033)          // outline 0xf0f2ff * 0.2
val errorContainerDark = Color(0xFF333030)   // errorContainer 0xfef2f2 * 0.2
val onSurfaceDark = Color(0xFFe6e8f0)        // Texto principal (claro, legible)
val onSurfaceVariantDark = Color(0xFF9098b1) // Texto secundario / iconos inactivos
