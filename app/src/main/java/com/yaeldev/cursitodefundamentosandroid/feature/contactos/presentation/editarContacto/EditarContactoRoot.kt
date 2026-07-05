package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.editarContacto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

@Composable
fun EditarContactoRoot(
    id: String,
    onClose: () -> Unit,
    onGuardado: () -> Unit,
    onEliminado: () -> Unit
) {
    val viewModel: EditarContactoViewModel = viewModel(
        factory = appContainer().contactos.factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        EditarContactoActions(
            onClose = onClose,
            onGuardar = {
                viewModel.guardar(onGuardado)
            },
            onDelete = {
                viewModel.eliminar(onEliminado)
            },
            onNombreChange = { nuevoValor ->
                viewModel.onNombreChange(nuevoValor)
            },
            onApellidoChange = { nuevoValor ->
                viewModel.onApellidoChange(nuevoValor)
            },
            onTelefonoChange = { nuevoValor ->
                viewModel.onTelefonoChange(nuevoValor)
            },
            onCorreoChange = { nuevoValor ->
                viewModel.onCorreoChange(nuevoValor)
            },
            onEmpresaChange = { nuevoValor ->
                viewModel.onEmpresaChange(nuevoValor)
            }
        )
    }

    LaunchedEffect(id) {
        viewModel.cargarContacto(id)
    }

    EditarContactoScreen(
        uiState = uiState,
        actions = actions
    )

}
