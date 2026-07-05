package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.favoritos

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.local.ContactosMuestra

sealed interface FavoritosUiState {
    data object Loading : FavoritosUiState
    data object Empty : FavoritosUiState
    data class Error(val message: String) : FavoritosUiState
    data class Success(
        val favoritos: List<Contacto> = emptyList()
    ) : FavoritosUiState
}

data class FavoritosActions(
    val onNavigateToContactos: () -> Unit = {},
    val onNavigateToAddContact: () -> Unit = {},
    val onNavigateToDetail: (Contacto) -> Unit = {},
    val onNavigateToChats: () -> Unit = {},
    val onNavigateToPerfil: () -> Unit = {},
    val onToggleFavorito: (Contacto) -> Unit = {}
)

class FavoritosPreviewParameterProvider : PreviewParameterProvider<FavoritosUiState> {
    override val values: Sequence<FavoritosUiState>
        get() = sequenceOf(
            FavoritosUiState.Loading,
            FavoritosUiState.Empty,
            FavoritosUiState.Error("No se pudieron cargar los favoritos."),
            FavoritosUiState.Success(
                favoritos = ContactosMuestra.lista.filter { it.favorite }
            )
        )
}
