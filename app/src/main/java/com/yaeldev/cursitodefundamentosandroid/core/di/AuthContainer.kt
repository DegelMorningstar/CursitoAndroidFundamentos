package com.yaeldev.cursitodefundamentosandroid.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.IniciarSesionUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.RegistrarUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.login.LoginViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.registro.RegistroViewModel

/**
 * DI del feature auth. Recibe el repositorio (instancia unica de la app) y expone
 * una factory compartida con un initializer por ViewModel.
 */
class AuthContainer(
    private val authRepository: AuthRepository
) {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { LoginViewModel(IniciarSesionUseCase(authRepository)) }
        initializer { RegistroViewModel(RegistrarUseCase(authRepository)) }
    }
}
