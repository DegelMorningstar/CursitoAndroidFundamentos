package com.yaeldev.cursitodefundamentosandroid.presentation.favoritos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.presentation.components.ContactoItem
import com.yaeldev.cursitodefundamentosandroid.presentation.listaContacto.navBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    modifier: Modifier = Modifier,
    uiState: FavoritosUiState,
    actions: FavoritosActions
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Favoritos",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = actions.onNavigateToContactos,
                    colors = navBarItemColors(),
                    icon = {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                    },
                    label = { Text("Contactos") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    colors = navBarItemColors(),
                    icon = {
                        Icon(Icons.Default.Star, contentDescription = null)
                    },
                    label = { Text("Favoritos") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = actions.onNavigateToAddContact,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar contacto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.favoritos) { contacto ->
                    ContactoItem(
                        fullName = contacto.nombreCompleto,
                        cellPhone = contacto.phone,
                        esFavorito = contacto.favorite,
                        onClick = {
                            actions.onNavigateToDetail(contacto)
                        },
                        onToggleFavorito = {
                            actions.onToggleFavorito(contacto)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoritosPreview(
    @PreviewParameter(FavoritosPreviewParameterProvider::class) state: FavoritosUiState
) {
    AppTheme {
        FavoritosScreen(
            uiState = state,
            actions = FavoritosActions()
        )
    }
}
