package com.yaeldev.cursitodefundamentosandroid.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.ui.theme.AppTheme

/**
 * §3.10 Snackbar — fondo onSurface, texto blanco, radius 12.
 * Cascarón visual: la visibilidad y el auto-dismiss se manejan despues.
 */
@Composable
fun AppSnackbar(
    modifier: Modifier = Modifier,
    message: String
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 6.dp
    ) {
        Text(
            text = message,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppSnackbarPreview() {
    AppTheme {
        AppSnackbar(message = "Contacto agregado ✓")
    }
}
