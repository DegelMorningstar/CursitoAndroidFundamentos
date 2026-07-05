package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.navigation

import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.login.LoginRoot
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.registro.RegistroRoot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.ListaContacto

fun NavGraphBuilder.authGraph(
    navController: NavHostController
){
    composable<Login> {
        LoginRoot(
            onAutenticado = {
                navController.navigate(ListaContacto) {
                    // Al entrar limpiamos el flujo de auth: el back no vuelve al login.
                    popUpTo(Login) { inclusive = true }
                }
            },
            onIrARegistro = {
                navController.navigate(Registro)
            }
        )
    }
    composable<Registro> {
        RegistroRoot(
            onRegistrado = {
                navController.navigate(ListaContacto) {
                    popUpTo(Login) { inclusive = true }
                }
            },
            onIrALogin = {
                navController.navigateUp()
            }
        )
    }
}