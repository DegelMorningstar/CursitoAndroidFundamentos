package com.yaeldev.cursitodefundamentosandroid.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaeldev.cursitodefundamentosandroid.ui.theme.AppTheme

/**
 * §3.11 Form Field — label uppercase + input + mensaje de error opcional.
 * Cascarón visual: value, onValueChange y error se conectan despues.
 */
@Composable
fun FormField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    placeholder: String = "",
    required: Boolean = false,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {}
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Text(
            text = if (required) "${label.uppercase()} *" else label.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.7.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
        if (error != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormFieldPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FormField(label = "Nombre", value = "Yael", required = true)
            FormField(label = "Telefono", placeholder = "10 digitos", required = true, error = "Campo obligatorio")
        }
    }
}
