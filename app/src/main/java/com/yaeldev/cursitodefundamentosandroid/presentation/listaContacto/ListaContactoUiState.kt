package com.yaeldev.cursitodefundamentosandroid.presentation.listaContacto

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.data.local.ContactosMuestra

sealed interface ListaContactoUiState {
    data object Empty : ListaContactoUiState
    data object Loading : ListaContactoUiState
    data class Error(val message:String) : ListaContactoUiState
    data class Success(
        val query: String = "",
        val contactos: List<Contacto> = emptyList()
    ): ListaContactoUiState {
        val contactosFiltrados : List<Contacto>
            get() = if(query.isBlank()) contactos
                    else contactos.filter {
                        it.nombreCompleto.contains(query, ignoreCase = true)
                    }
        val mostrarLista: Boolean
            get() = contactosFiltrados.isNotEmpty()
    }

}

data class ListaContactoActions(
    val onNavigateToAddContact: () -> Unit = {},
    val onNavigateToDetail: (Contacto) -> Unit = {},
    val onNavigateToFavoritos: () -> Unit = {},
    val onToggleFavorito: (Contacto) -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val onClear: () -> Unit = {}
)


class ListaContactoPreviewParameterProvider: PreviewParameterProvider<ListaContactoUiState> {
    override val values: Sequence<ListaContactoUiState>
        get() = sequenceOf(
            ListaContactoUiState.Empty,
            ListaContactoUiState.Loading,
            ListaContactoUiState.Error("No se encontraron contactos."),
            ListaContactoUiState.Success(
                query = "Yael M",
                contactos = ContactosMuestra.lista
            ),
            ListaContactoUiState.Success(
                contactos = ContactosMuestra.lista
            ),
            ListaContactoUiState.Success(
                contactos = emptyList()
            )
        )
}
