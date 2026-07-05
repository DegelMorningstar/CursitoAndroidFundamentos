package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.detallePerfil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

@Composable
fun DetallePerfilRoot(
    uid: String,
    onBack: () -> Unit
) {
    val viewModel: DetallePerfilViewModel = viewModel(factory = appContainer().perfil.factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        DetallePerfilActions(onBack = onBack)
    }

    LaunchedEffect(uid) {
        viewModel.cargarPerfil(uid)
    }

    DetallePerfilScreen(
        uiState = uiState,
        actions = actions
    )
}
