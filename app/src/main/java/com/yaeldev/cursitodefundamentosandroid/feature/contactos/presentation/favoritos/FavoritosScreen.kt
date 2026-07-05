package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.favoritos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppBottomBar
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppTab
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.ContactoItem
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.EmptyFavoritos

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
                    containerColor = MaterialTheme.colorScheme.surface,
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
            AppBottomBar(
                seleccionada = AppTab.Favoritos,
                onContactos = actions.onNavigateToContactos,
                onChats = actions.onNavigateToChats,
                onPerfil = actions.onNavigateToPerfil
            )
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
            when (uiState) {
                FavoritosUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                FavoritosUiState.Empty -> EmptyFavoritos()
                is FavoritosUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(uiState.message)
                    }
                }
                is FavoritosUiState.Success -> {
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
