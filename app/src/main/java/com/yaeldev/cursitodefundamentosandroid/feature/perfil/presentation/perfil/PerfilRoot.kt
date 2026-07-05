package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.perfil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@Composable
fun PerfilRoot(
    temaOscuro: Boolean,
    onToggleTema: (Boolean) -> Unit,
    onSesionCerrada: () -> Unit,
    onNavigateToContactos: () -> Unit,
    onNavigateToFavoritos: () -> Unit,
    onNavigateToChats: () -> Unit,
    mostrarMensaje: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: PerfilViewModel = viewModel(factory = appContainer().perfil.factory)
    val estadoVm by viewModel.uiState.collectAsStateWithLifecycle()
    // El tema es estado de app (no del ViewModel): lo inyectamos al estado que ve la pantalla.
    val uiState = estadoVm.copy(temaOscuro = temaOscuro)

    // Puente del evento de exito (VM) al snackbar de la app; se consume una vez.
    LaunchedEffect(estadoVm.mensaje) {
        estadoVm.mensaje?.let {
            mostrarMensaje(it)
            viewModel.mensajeConsumido()
        }
    }

    // Selector de imagen (Photo Picker): al elegir, comprimimos a base64 en IO.
    val selectorFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val base64 = uriABase64(context, uri)
                if (base64 != null) viewModel.onFotoSeleccionada(base64)
                else viewModel.mostrarError("No se pudo procesar la imagen.")
            }
        }
    }

    val actions = remember(viewModel) {
        PerfilActions(
            onNombreChange = { viewModel.onNombreChange(it) },
            onEstadoChange = { viewModel.onEstadoChange(it) },
            onCambiarFoto = {
                selectorFoto.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onGuardar = { viewModel.guardar() },
            onToggleTema = onToggleTema,
            onCerrarSesion = { viewModel.cerrarSesion(onSesionCerrada) },
            onNavigateToContactos = onNavigateToContactos,
            onNavigateToFavoritos = onNavigateToFavoritos,
            onNavigateToChats = onNavigateToChats
        )
    }

    PerfilScreen(
        uiState = uiState,
        actions = actions
    )
}

/**
 * Convierte la imagen elegida a un thumbnail base64: la reduce a 256 px de lado
 * mayor y la comprime a JPEG 70% (un avatar queda en ~20-40 KB, muy debajo del
 * tope de 1 MiB del documento de Firestore). Corre en IO. null si algo falla.
 */
private suspend fun uriABase64(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    try {
        val original = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return@withContext null
        val escalado = escalarMax(original, 256)
        val salida = ByteArrayOutputStream()
        escalado.compress(Bitmap.CompressFormat.JPEG, 70, salida)
        Base64.encodeToString(salida.toByteArray(), Base64.NO_WRAP)
    } catch (e: Exception) {
        null
    }
}

/** Reduce el bitmap para que su lado mayor no exceda [maxLado], conservando proporcion. */
private fun escalarMax(bitmap: Bitmap, maxLado: Int): Bitmap {
    val ancho = bitmap.width
    val alto = bitmap.height
    if (ancho <= maxLado && alto <= maxLado) return bitmap
    val factor = maxLado.toFloat() / maxOf(ancho, alto)
    return Bitmap.createScaledBitmap(bitmap, (ancho * factor).toInt(), (alto * factor).toInt(), true)
}
