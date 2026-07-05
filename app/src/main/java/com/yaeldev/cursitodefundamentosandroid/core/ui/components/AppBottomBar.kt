package com.yaeldev.cursitodefundamentosandroid.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/** Pestañas del bottom bar de la app. */
enum class AppTab { Contactos, Favoritos, Chats, Perfil }

/**
 * Barra de navegacion inferior compartida por Contactos, Favoritos y Perfil.
 * Cada pantalla indica su [seleccionada] y pasa las lambdas de navegacion; la
 * pestaña activa normalmente recibe un callback vacio.
 */
@Composable
fun AppBottomBar(
    seleccionada: AppTab,
    onContactos: () -> Unit = {},
    onFavoritos: () -> Unit = {},
    onChats: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = seleccionada == AppTab.Contactos,
            onClick = onContactos,
            colors = colores(),
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
            label = { Text("Contactos") }
        )
        NavigationBarItem(
            selected = seleccionada == AppTab.Favoritos,
            onClick = onFavoritos,
            colors = colores(),
            icon = { Icon(Icons.Filled.Star, contentDescription = null) },
            label = { Text("Favoritos") }
        )
        NavigationBarItem(
            selected = seleccionada == AppTab.Chats,
            onClick = onChats,
            colors = colores(),
            icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) },
            label = { Text("Chats") }
        )
        NavigationBarItem(
            selected = seleccionada == AppTab.Perfil,
            onClick = onPerfil,
            colors = colores(),
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            label = { Text("Perfil") }
        )
    }
}

@Composable
private fun colores() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)
