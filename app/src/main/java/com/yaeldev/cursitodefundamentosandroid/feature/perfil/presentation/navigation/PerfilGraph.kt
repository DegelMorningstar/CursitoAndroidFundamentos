package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.navigation.Login
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.navigation.ListaChats
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.Favoritos
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.ListaContacto
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.detallePerfil.DetallePerfilRoot
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.perfil.PerfilRoot
import com.yaeldev.cursitodefundamentosandroid.navigateToTab

fun NavGraphBuilder.perfilGraph(
    navController: NavHostController,
    temaOscuro: Boolean,
    mostrarMensaje: (String) -> Unit,
    onToggleTema: (Boolean) -> Unit,
){
    composable<Perfil> {
        PerfilRoot(
            temaOscuro = temaOscuro,
            onToggleTema = onToggleTema,
            onSesionCerrada = {
                navController.navigate(Login) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            },
            onNavigateToContactos = {
                navController.navigateToTab(ListaContacto)
            },
            onNavigateToFavoritos = {
                navController.navigateToTab(Favoritos)
            },
            onNavigateToChats = {
                navController.navigateToTab(ListaChats)
            },
            mostrarMensaje = mostrarMensaje
        )
    }
    composable<DetallePerfil> { backStackEntry ->
        val ruta = backStackEntry.toRoute<DetallePerfil>()
        DetallePerfilRoot(
            uid = ruta.uid,
            onBack = {
                navController.navigateUp()
            }
        )
    }
}