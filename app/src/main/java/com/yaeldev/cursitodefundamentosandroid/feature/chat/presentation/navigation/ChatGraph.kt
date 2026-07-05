package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat.ChatRoot
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.listaChats.ListaChatsRoot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.Favoritos
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.ListaContacto
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.navigation.DetallePerfil
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.navigation.Perfil
import com.yaeldev.cursitodefundamentosandroid.navigateToTab

fun NavGraphBuilder.chatGraph(
    navController: NavHostController,
){
    composable<ListaChats> {
        ListaChatsRoot(
            onAbrirChat = { chatId, titulo, otroUid ->
                navController.navigate(
                    Conversacion(chatId = chatId, titulo = titulo, otroUid = otroUid)
                )
            },
            onNavigateToContactos = {
                navController.navigateToTab(ListaContacto)
            },
            onNavigateToFavoritos = {
                navController.navigateToTab(Favoritos)
            },
            onNavigateToPerfil = {
                navController.navigateToTab(Perfil)
            }
        )
    }
    composable<Conversacion> { backStackEntry ->
        val ruta = backStackEntry.toRoute<Conversacion>()
        ChatRoot(
            chatId = ruta.chatId,
            titulo = ruta.titulo,
            onBack = {
                navController.navigateUp()
            },
            onVerPerfil = {
                navController.navigate(DetallePerfil(uid = ruta.otroUid))
            }
        )
    }
}