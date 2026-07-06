package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

/**
 * Stateful: obtiene el [SplashViewModel], dispara la decision y, cuando esta lista,
 * navega. Las lambdas de navegacion son parametros (no toca el NavController).
 */
@Composable
fun SplashRoot(
    haySesion: Boolean,
    onIrOnboarding: () -> Unit,
    onIrLogin: () -> Unit,
    onIrHome: () -> Unit
) {
    val viewModel: SplashViewModel = viewModel(factory = appContainer().onboarding.factory)
    val destino by viewModel.destino.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.decidir(haySesion)
    }

    LaunchedEffect(destino) {
        when (destino) {
            SplashDestino.Onboarding -> onIrOnboarding()
            SplashDestino.Login -> onIrLogin()
            SplashDestino.Home -> onIrHome()
            null -> Unit
        }
    }

    SplashScreen()
}
