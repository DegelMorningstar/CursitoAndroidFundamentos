package com.yaeldev.cursitodefundamentosandroid.presentation.favoritos

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.data.local.ContactosMuestra

data class FavoritosUiState(
    val favoritos: List<Contacto> = emptyList()
) {
    val mostrarLista: Boolean = favoritos.isNotEmpty()
}

data class FavoritosActions(
    val onNavigateToContactos: () -> Unit = {},
    val onNavigateToAddContact: () -> Unit = {},
    val onNavigateToDetail: (Contacto) -> Unit = {},
    val onToggleFavorito: (Contacto) -> Unit = {}
)

class FavoritosPreviewParameterProvider : PreviewParameterProvider<FavoritosUiState> {
    override val values: Sequence<FavoritosUiState>
        get() = sequenceOf(
            FavoritosUiState(
                favoritos = ContactosMuestra.lista.filter { it.favorite }
            ),
            FavoritosUiState(
                favoritos = emptyList()
            )
        )
}
