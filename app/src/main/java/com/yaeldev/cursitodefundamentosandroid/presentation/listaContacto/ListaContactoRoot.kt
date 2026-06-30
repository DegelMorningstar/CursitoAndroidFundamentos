package com.yaeldev.cursitodefundamentosandroid.presentation.listaContacto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.data.local.ContactoRepositoryFake
import com.yaeldev.cursitodefundamentosandroid.data.remote.AppService
import com.yaeldev.cursitodefundamentosandroid.data.remote.ContactoRepositoryImpl

@Composable
fun ListaContactoRoot(
    onNavigateToAddContact: () -> Unit,
    onNavigateToDetail: (Contacto) -> Unit,
    onNavigateToFavoritos: () -> Unit
) {
    val randomUserAPI = remember { AppService.randomUserApi }
    val repository = remember { ContactoRepositoryImpl(randomUserAPI) }
    val factory = remember { ListaContactoViewModelFactory(repository) }
    val viewModel: ListaContactoViewModel = viewModel(
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        ListaContactoActions(
            onNavigateToAddContact = onNavigateToAddContact,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToFavoritos = onNavigateToFavoritos,
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