package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.listaContacto

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
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.EmptyContactos
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContactoScreen(
    modifier: Modifier = Modifier,
    uiState: ListaContactoUiState,
    actions: ListaContactoActions
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
                        text = "Contactos",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                seleccionada = AppTab.Contactos,
                onFavoritos = actions.onNavigateToFavoritos,
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
            when(uiState){
                ListaContactoUiState.Empty -> Unit
                is ListaContactoUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(uiState.message)
                    }
                }
                ListaContactoUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                is ListaContactoUiState.Success -> {
                    SearchBar(
                        query = uiState.query,
                        onQueryChange = actions.onQueryChange,
                        onClear = actions.onClear
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp)
                    ) {
                        if(uiState.mostrarLista) {
                            items(uiState.contactosFiltrados) { contacto ->
                                ContactoItem(
                                    fullName = contacto.nombreCompleto,
                                    cellPhone = contacto.phone,
                                    esFavorito = contacto.favorite,
                                    onClick = {
                                        actions.onNavigateToDetail.invoke(contacto)
                                    },
                                    onToggleFavorito = {
                                        actions.onToggleFavorito(contacto)
                                    }
                                )
                            }
                        }else{
                            item {
                                EmptyContactos()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListaContactoPreview(
    @PreviewParameter(ListaContactoPreviewParameterProvider::class) state: ListaContactoUiState
) {
    AppTheme {
        Column {
            ListaContactoScreen(
                uiState = state,
                actions = ListaContactoActions()
            )
        }
    }
}