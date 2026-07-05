package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.favoritos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.firestore.ContactoRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerFavoritosUseCase

@Composable
fun FavoritosRoot(
    onNavigateToContactos: () -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToDetail: (Contacto) -> Unit,
    onNavigateToChats: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val repository = remember { ContactoRepositoryFirestore() }
    val factory = remember {
        FavoritosViewModelFactory(
            ObtenerFavoritosUseCase(repository),
            AlternarFavoritoUseCase(repository)
        )
    }
    val viewModel: FavoritosViewModel = viewModel(
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        FavoritosActions(
            onNavigateToContactos = onNavigateToContactos,
            onNavigateToAddContact = onNavigateToAddContact,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToChats = onNavigateToChats,
            onNavigateToPerfil = onNavigateToPerfil,
            onToggleFavorito = { contacto ->
                viewModel.alternarFavorito(contacto.id)
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getFavoritos()
    }

    FavoritosScreen(
        uiState = uiState,
        actions = actions
    )

}
