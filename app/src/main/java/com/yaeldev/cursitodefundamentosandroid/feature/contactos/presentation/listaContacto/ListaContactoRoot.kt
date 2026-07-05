package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.listaContacto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.firestore.ContactoRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactosUseCase

@Composable
fun ListaContactoRoot(
    onNavigateToAddContact: () -> Unit,
    onNavigateToDetail: (Contacto) -> Unit,
    onNavigateToFavoritos: () -> Unit,
    onNavigateToChats: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val repository = remember { ContactoRepositoryFirestore() }
    val factory = remember {
        ListaContactoViewModelFactory(
            ObtenerContactosUseCase(repository),
            AlternarFavoritoUseCase(repository)
        )
    }
    val viewModel: ListaContactoViewModel = viewModel(
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        ListaContactoActions(
            onNavigateToAddContact = onNavigateToAddContact,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToFavoritos = onNavigateToFavoritos,
            onNavigateToChats = onNavigateToChats,
            onNavigateToPerfil = onNavigateToPerfil,
            onToggleFavorito = { contacto ->
                viewModel.alternarFavorito(contacto.id)
            },
            onQueryChange = { nuevoValor ->
                viewModel.onQueryChange(nuevoValor)
            },
            onClear = {
                viewModel.onClear()
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getContactList()
    }

    ListaContactoScreen(
        uiState = uiState,
        actions = actions
    )

}