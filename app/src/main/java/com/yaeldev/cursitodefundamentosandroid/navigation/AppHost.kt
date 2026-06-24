package com.yaeldev.cursitodefundamentosandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yaeldev.cursitodefundamentosandroid.data.ContactosMuestra
import com.yaeldev.cursitodefundamentosandroid.views.AgregarContactoScreen
import com.yaeldev.cursitodefundamentosandroid.views.DetalleContactoScreen
import com.yaeldev.cursitodefundamentosandroid.views.EditarContactoScreen
import com.yaeldev.cursitodefundamentosandroid.views.FavoritosScreen
import com.yaeldev.cursitodefundamentosandroid.views.listaContacto.ListaContactoRoot
import com.yaeldev.cursitodefundamentosandroid.views.listaContacto.ListaContactoScreen

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
            // TODO(viewmodel): resolver el contacto via repositorio en lugar de ContactosMuestra.
            val contacto = ContactosMuestra.porId(ruta.id)
            EditarContactoScreen(
                nombre = contacto?.first ?: "",
                apellido = contacto?.last ?: "",
                telefono = contacto?.phone ?: "",
                correo = contacto?.email ?: "",
                empresa = contacto?.company ?: "",
                onClose = {
                    navController.navigateUp()
                },
                onGuardar = {
                    navController.navigateUp()
                },
                onDelete = {
                    navController.popBackStack(
                        navController.graph.findStartDestination().id,
                        inclusive = false
                    )
                }
            )
        }
        composable<AgregaContacto> {
            AgregarContactoScreen(
                onClose = {
                    navController.navigateUp()
                },
                onGuardar = {
                    navController.navigateUp()
                }
            )
        }
        composable<DetalleContacto> { backStackEntry ->
            val ruta = backStackEntry.toRoute<DetalleContacto>()
            // TODO(viewmodel): resolver el contacto via repositorio en lugar de ContactosMuestra.
            val contacto = ContactosMuestra.porId(ruta.id)
            DetalleContactoScreen(
                fullName = contacto?.nombreCompleto ?: "",
                initials = contacto?.iniciales ?: "?",
                company = contacto?.company ?: "",
                phone = contacto?.phone ?: "",
                email = contacto?.email ?: "",
                esFavorito = contacto?.favorite ?: false,
                onBack = {
                    navController.navigateUp()
                },
                onEdit = {
                    navController.navigate(EditaContacto(id = ruta.id))
                },
                onDelete = {
                    navController.popBackStack(
                        navController.graph.findStartDestination().id,
                        inclusive = false
                    )
                }
            )
        }
        composable<Favoritos> {
            FavoritosScreen(
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