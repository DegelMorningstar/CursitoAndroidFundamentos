package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.agregarContacto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

@Composable
fun AgregarContactoRoot(
    onClose: () -> Unit,
    onGuardado: () -> Unit
) {
    val viewModel: AgregarContactoViewModel = viewModel(
        factory = appContainer().contactos.factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        AgregarContactoActions(
            onClose = onClose,
            onGuardar = {
                viewModel.guardar(onGuardado)
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

    AgregarContactoScreen(
        uiState = uiState,
        actions = actions
    )

}
