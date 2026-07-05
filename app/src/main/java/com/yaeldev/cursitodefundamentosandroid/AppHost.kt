package com.yaeldev.cursitodefundamentosandroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.navigation.Login
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.navigation.authGraph
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.navigation.chatGraph
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.contactosGraph
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.navigation.perfilGraph

@Composable
fun AppHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination:Any = Login,
    temaOscuro: Boolean = false,
    onToggleTema: (Boolean) -> Unit = {},
    mostrarMensaje: (String) -> Unit = {},
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        contactosGraph(navController,mostrarMensaje)
        perfilGraph(navController,temaOscuro,mostrarMensaje,onToggleTema)
        chatGraph(navController)
    }
}

fun NavHostController.navigateToTab(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
