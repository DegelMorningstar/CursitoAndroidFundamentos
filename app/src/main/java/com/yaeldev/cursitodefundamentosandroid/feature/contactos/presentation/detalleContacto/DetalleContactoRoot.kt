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
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.firestore.ContactoRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.EliminarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactoPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.BuscarUsuarioPorEmailUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.firestore.ChatRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.AbrirOCrearChatUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase

@Composable
fun DetalleContactoRoot(
    id: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onEliminado: () -> Unit,
    onNavigateToChat: (chatId: String, titulo: String, otroUid: String) -> Unit
) {
    val context = LocalContext.current
    val repository = remember { ContactoRepositoryFirestore() }
    val perfilRepository = remember { PerfilRepositoryFirebase() }
    val chatRepository = remember { ChatRepositoryFirestore() }
    val factory = remember {
        DetalleContactoViewModelFactory(
            ObtenerContactoPorIdUseCase(repository),
            AlternarFavoritoUseCase(repository),
            EliminarContactoUseCase(repository),
            BuscarUsuarioPorEmailUseCase(perfilRepository),
            AbrirOCrearChatUseCase(chatRepository)
        )
    }
    val viewModel: DetalleContactoViewModel = viewModel(
        factory = factory
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
