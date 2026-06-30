package com.yaeldev.cursitodefundamentosandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yaeldev.cursitodefundamentosandroid.presentation.agregarContacto.AgregarContactoRoot
import com.yaeldev.cursitodefundamentosandroid.presentation.detalleContacto.DetalleContactoRoot
import com.yaeldev.cursitodefundamentosandroid.presentation.editarContacto.EditarContactoRoot
import com.yaeldev.cursitodefundamentosandroid.presentation.favoritos.FavoritosRoot
import com.yaeldev.cursitodefundamentosandroid.presentation.listaContacto.ListaContactoRoot

@Composable
fun AppHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination:Any = ListaContacto,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<ListaContacto> {
            ListaContactoRoot(
                onNavigateToAddContact = {
                    navController.navigate(AgregaContacto)
                },
                onNavigateToDetail = { contacto ->
                    navController.navigate(DetalleContacto(id = contacto.id))
                },
                onNavigateToFavoritos = {
                    navController.navigateToTab(Favoritos)
                }
            )
        }
        composable<EditaContacto> { backStackEntry ->
            val ruta = backStackEntry.toRoute<EditaContacto>()
            EditarContactoRoot(
                id = ruta.id,
                onClose = {
                    navController.navigateUp()
                },
                onGuardado = {
                    navController.navigateUp()
                },
                onEliminado = {
                    navController.popBackStack(
                        navController.graph.findStartDestination().id,
                        inclusive = false
                    )
                }
            )
        }
        composable<AgregaContacto> {
            AgregarContactoRoot(
                onClose = {
                    navController.navigateUp()
                },
                onGuardado = {
                    navController.navigateUp()
                }
            )
        }
        composable<DetalleContacto> { backStackEntry ->
            val ruta = backStackEntry.toRoute<DetalleContacto>()
            DetalleContactoRoot(
                id = ruta.id,
                onBack = {
                    navController.navigateUp()
                },
                onEdit = {
                    navController.navigate(EditaContacto(id = ruta.id))
                },
                onEliminado = {
                    navController.popBackStack(
                        navController.graph.findStartDestination().id,
                        inclusive = false
                    )
                }
            )
        }
        composable<Favoritos> {
            FavoritosRoot(
                onNavigateToContactos = {
                    navController.navigateToTab(ListaContacto)
                },
                onNavigateToAddContact = {
                    navController.navigate(AgregaContacto)
                },
                onNavigateToDetail = { contacto ->
                    navController.navigate(DetalleContacto(id = contacto.id))
                }
            )
        }
    }
}

/**
 * Navegacion entre pestañas del bottom bar (Contactos / Favoritos): vuelve al
 * inicio del grafo en vez de apilar destinos, y evita duplicados con
 * launchSingleTop. Asi el back siempre regresa a la lista, no a la pestaña previa.
 */
private fun NavHostController.navigateToTab(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
