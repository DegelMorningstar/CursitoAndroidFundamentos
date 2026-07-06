package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.navigation.Login
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.ListaContacto
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding.OnboardingRoot
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.splash.SplashRoot

/**
 * Rutas de arranque: [Splash] (branding + decision) y [Onboarding] (bienvenida).
 * [haySesion] lo provee `MainActivity` (FirebaseAuth.currentUser) para elegir el
 * destino final. Ambas rutas se sacan del back stack al navegar (popUpTo inclusive).
 */
fun NavGraphBuilder.onboardingGraph(
    navController: NavHostController,
    haySesion: Boolean
) {
    composable<Splash> {
        SplashRoot(
            haySesion = haySesion,
            onIrOnboarding = {
                navController.navigate(Onboarding) { popUpTo(Splash) { inclusive = true } }
            },
            onIrLogin = {
                navController.navigate(Login) { popUpTo(Splash) { inclusive = true } }
            },
            onIrHome = {
                navController.navigate(ListaContacto) { popUpTo(Splash) { inclusive = true } }
            }
        )
    }
    composable<Onboarding> {
        OnboardingRoot(
            onTerminado = {
                val destino: Any = if (haySesion) ListaContacto else Login
                navController.navigate(destino) { popUpTo(Onboarding) { inclusive = true } }
            }
        )
    }
}
