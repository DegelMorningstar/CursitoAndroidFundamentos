package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.detalleContacto

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

@Composable
fun DetalleContactoRoot(
    id: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onEliminado: () -> Unit,
    onNavigateToChat: (chatId: String, titulo: String, otroUid: String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: DetalleContactoViewModel = viewModel(
        factory = appContainer().contactos.factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        DetalleContactoActions(
            onBack = onBack,
            onEdit = onEdit,
            onShare = { texto ->
                val envio = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, texto)
                }
                lanzarIntent(context, Intent.createChooser(envio, "Compartir contacto"))
            },
            onDelete = {
                viewModel.eliminar(onEliminado)
            },
            onCall = { phone ->
                lanzarIntent(context, Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
            },
            onEmail = { email ->
                lanzarIntent(context, Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")))
            },
            onToggleFavorite = viewModel::alternarFavorito,
            onMensaje = { viewModel.abrirChat(onNavigateToChat) }
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

/** Lanza un intent implicito y avisa si no hay app que lo pueda atender. */
private fun lanzarIntent(context: android.content.Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No hay una app para esta accion", Toast.LENGTH_SHORT).show()
    }
}
