package com.yaeldev.cursitodefundamentosandroid.presentation.detalleContacto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.data.local.ContactoRepositoryFake

@Composable
fun DetalleContactoRoot(
    id: Int,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onEliminado: () -> Unit,
    onShare: () -> Unit = {}
) {
    val repository = remember { ContactoRepositoryFake() }
    val factory = remember { DetalleContactoViewModelFactory(repository) }
    val viewModel: DetalleContactoViewModel = viewModel(
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        DetalleContactoActions(
            onBack = onBack,
            onEdit = onEdit,
            onShare = onShare,
            onDelete = {
                viewModel.eliminar(onEliminado)
            }
        )
    }

    LaunchedEffect(id) {
        viewModel.cargarContacto(id)
    }

    DetalleContactoScreen(
        uiState = uiState,
        actions = actions
    )

}
