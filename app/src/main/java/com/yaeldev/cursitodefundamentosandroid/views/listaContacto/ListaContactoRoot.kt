package com.yaeldev.cursitodefundamentosandroid.views.listaContacto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.data.Contacto
import com.yaeldev.cursitodefundamentosandroid.viewmodels.ListaContactoViewModel

@Composable
fun ListaContactoRoot(
    onNavigateToAddContact: () -> Unit,
    onNavigateToDetail: (Contacto) -> Unit,
    onNavigateToFavoritos: () -> Unit
) {

    val viewModel: ListaContactoViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        ListaContactoActions(
            onNavigateToAddContact = onNavigateToAddContact,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToFavoritos = onNavigateToFavoritos,
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