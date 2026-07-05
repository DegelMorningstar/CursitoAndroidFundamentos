package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.navigation.Conversacion
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.navigation.ListaChats
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.agregarContacto.AgregarContactoRoot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.detalleContacto.DetalleContactoRoot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.editarContacto.EditarContactoRoot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.favoritos.FavoritosRoot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.listaContacto.ListaContactoRoot
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.navigation.Perfil
import com.yaeldev.cursitodefundamentosandroid.navigateToTab

fun NavGraphBuilder.contactosGraph(
    navController: NavHostController,
    mostrarMensaje: (String) -> Unit,
){
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
            },
            onNavigateToChats = {
                navController.navigateToTab(ListaChats)
            },
            onNavigateToPerfil = {
                navController.navigateToTab(Perfil)
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
                mostrarMensaje("Contacto actualizado")
                navController.navigateUp()
            },
            onEliminado = {
                mostrarMensaje("Contacto eliminado")
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
                mostrarMensaje("Contacto creado")
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
                mostrarMensaje("Contacto eliminado")
                navController.popBackStack(
                    navController.graph.findStartDestination().id,
                    inclusive = false
                )
            },
            onNavigateToChat = { chatId, titulo, otroUid ->
                navController.navigate(
                    Conversacion(chatId = chatId, titulo = titulo, otroUid = otroUid)
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
            },
            onNavigateToChats = {
                navController.navigateToTab(ListaChats)
            },
            onNavigateToPerfil = {
                navController.navigateToTab(Perfil)
            }
        )
    }
}