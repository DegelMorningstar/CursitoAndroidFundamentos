package com.yaeldev.cursitodefundamentosandroid.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.AppTheme

@Composable
fun FormularioContactoCard(
    modifier: Modifier = Modifier,
    nombre: String = "",
    apellido: String = "",
    telefono: String = "",
    correo: String = "",
    empresa: String = "",
    errorNombre: String? = null,
    errorTelefono: String? = null,
    onNombreChange: (String) -> Unit = {},
    onApellidoChange: (String) -> Unit = {},
    onTelefonoChange: (String) -> Unit = {},
    onCorreoChange: (String) -> Unit = {},
    onEmpresaChange: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            FormField(
                label = "Nombre",
                value = nombre,
                placeholder = "Nombre",
                required = true,
                error = errorNombre,
                onValueChange = onNombreChange
            )
            FormField(
                label = "Apellido",
                value = apellido,
                placeholder = "Apellido",
                onValueChange = onApellidoChange
            )
            FormField(
                label = "Telefono",
                value = telefono,
                placeholder = "10 digitos",
                required = true,
                error = errorTelefono,
                keyboardType = KeyboardType.Phone,
                onValueChange = onTelefonoChange
            )
            FormField(
                label = "Correo",
                value = correo,
                placeholder = "correo@ejemplo.com",
                keyboardType = KeyboardType.Email,
                onValueChange = onCorreoChange
            )
            FormField(
                label = "Empresa",
                value = empresa,
                placeholder = "Empresa",
                onValueChange = onEmpresaChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormularioContactoCardPreview() {
    AppTheme {
        FormularioContactoCard(
            modifier = Modifier.padding(16.dp),
            nombre = "Yael",
            telefono = "7771234568"
        )
    }
}
