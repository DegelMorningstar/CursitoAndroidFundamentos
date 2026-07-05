package com.yaeldev.cursitodefundamentosandroid.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.CerrarSesionUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories.PerfilRepository
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ActualizarPerfilUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ObtenerPerfilUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ObtenerUsuarioActualUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ObtenerUsuarioPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.detallePerfil.DetallePerfilViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.perfil.PerfilViewModel

/**
 * DI del feature perfil. Recibe el repo de perfil y el de auth (para cerrar sesion,
 * cuyo caso de uso vive en auth).
 */
class PerfilContainer(
    private val perfilRepository: PerfilRepository,
    private val authRepository: AuthRepository
) {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            PerfilViewModel(
                ObtenerUsuarioActualUseCase(perfilRepository),
                ObtenerPerfilUseCase(perfilRepository),
                ActualizarPerfilUseCase(perfilRepository),
                CerrarSesionUseCase(authRepository)
            )
        }
        initializer { DetallePerfilViewModel(ObtenerUsuarioPorIdUseCase(perfilRepository)) }
    }
}
