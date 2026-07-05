package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.editarContacto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.ConfirmDialog
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.DeleteButton
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.FormularioContactoCard
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.GuardarButton
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.MensajeError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarContactoScreen(
    modifier: Modifier = Modifier,
    uiState: EditarContactoUiState,
    actions: EditarContactoActions
) {
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    if (mostrarDialogoEliminar) {
        ConfirmDialog(
            onDismiss = { mostrarDialogoEliminar = false },
            onConfirm = {
                mostrarDialogoEliminar = false
                actions.onDelete()
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = actions.onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                    }
                },
                title = {
                    Text(text = "Editar contacto", style = MaterialTheme.typography.headlineLarge)
                },
                actions = {
                    GuardarButton(onClick = actions.onGuardar)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            MensajeError(mensaje = uiState.error)
            if (uiState.error != null) Spacer(modifier = Modifier.height(16.dp))
            FormularioContactoCard(
                nombre = uiState.nombre,
                apellido = uiState.apellido,
                telefono = uiState.telefono,
                correo = uiState.correo,
                empresa = uiState.empresa,
                errorNombre = uiState.errorNombre,
                errorTelefono = uiState.errorTelefono,
                onNombreChange = actions.onNombreChange,
                onApellidoChange = actions.onApellidoChange,
                onTelefonoChange = actions.onTelefonoChange,
                onCorreoChange = actions.onCorreoChange,
                onEmpresaChange = actions.onEmpresaChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            DeleteButton(onClick = { mostrarDialogoEliminar = true })
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditarContactoPreview(
    @PreviewParameter(EditarContactoPreviewParameterProvider::class) state: EditarContactoUiState
) {
    AppTheme {
        EditarContactoScreen(
            uiState = state,
            actions = EditarContactoActions()
        )
    }
}
