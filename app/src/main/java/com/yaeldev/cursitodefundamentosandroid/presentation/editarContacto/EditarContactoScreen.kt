package com.yaeldev.cursitodefundamentosandroid.presentation.editarContacto

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.presentation.components.DeleteButton
import com.yaeldev.cursitodefundamentosandroid.presentation.components.FormularioContactoCard
import com.yaeldev.cursitodefundamentosandroid.presentation.components.GuardarButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarContactoScreen(
    modifier: Modifier = Modifier,
    uiState: EditarContactoUiState,
    actions: EditarContactoActions
) {
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
            DeleteButton(onClick = actions.onDelete)
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
