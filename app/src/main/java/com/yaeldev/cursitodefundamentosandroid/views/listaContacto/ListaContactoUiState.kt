package com.yaeldev.cursitodefundamentosandroid.views.listaContacto

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.data.Contacto
import com.yaeldev.cursitodefundamentosandroid.data.ContactosMuestra

data class ListaContactoUiState(
    val query: String = "",
    val contactos: List<Contacto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
){
    val contactosFiltrados = if(query.isNotEmpty()) {
        contactos.filter { it.nombreCompleto.contains(query, ignoreCase = true) }
    } else {
        contactos
    }
    val mostrarLista: Boolean = contactos.isNotEmpty()
}

data class ListaContactoActions(
    val onNavigateToAddContact: () -> Unit = {},
    val onNavigateToDetail: (Contacto) -> Unit = {},
    val onNavigateToFavoritos: () -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val onClear: () -> Unit = {}
)


class ListaContactoPreviewParameterProvider: PreviewParameterProvider<ListaContactoUiState> {
    override val values: Sequence<ListaContactoUiState>
        get() = sequenceOf(
            ListaContactoUiState(
                query = "Yael M",
                contactos = ContactosMuestra.lista
            ),
            ListaContactoUiState(
                contactos = ContactosMuestra.lista
            ),
            ListaContactoUiState(
                contactos = emptyList()
            ),
            ListaContactoUiState(
                isLoading = true
            )
        )
}
