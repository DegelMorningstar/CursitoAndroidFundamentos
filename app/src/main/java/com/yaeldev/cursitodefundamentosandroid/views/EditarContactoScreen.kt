package com.yaeldev.cursitodefundamentosandroid.views

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
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.views.components.DeleteButton
import com.yaeldev.cursitodefundamentosandroid.views.components.FormularioContactoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarContactoScreen(
    modifier: Modifier = Modifier,
    nombre: String = "Yael",
    apellido: String = "Montes Camacho",
    telefono: String = "7771234568",
    correo: String = "yael@correo.com",
    empresa: String = "YaelDev",
    onClose: () -> Unit = {},
    onGuardar: () -> Unit = {},
    onDelete: () -> Unit = {}
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
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                    }
                },
                title = {
                    Text(text = "Editar contacto", style = MaterialTheme.typography.headlineLarge)
                },
                actions = {
                    GuardarButton(onClick = onGuardar)
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
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                correo = correo,
                empresa = empresa
            )
            Spacer(modifier = Modifier.height(24.dp))
            DeleteButton(onClick = onDelete)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditarContactoPreview() {
    AppTheme {
        EditarContactoScreen()
    }
}
