package com.yaeldev.cursitodefundamentosandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yaeldev.cursitodefundamentosandroid.views.AgregarContactoScreen
import com.yaeldev.cursitodefundamentosandroid.views.DetalleContactoScreen
import com.yaeldev.cursitodefundamentosandroid.views.EditarContactoScreen
import com.yaeldev.cursitodefundamentosandroid.views.FavoritosScreen
import com.yaeldev.cursitodefundamentosandroid.views.ListaContactoScreen

@Composable
fun AppHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination:Any = ListaContacto
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<ListaContacto> {
            ListaContactoScreen(
                onNavigateToAddContact = {
                    navController.navigate(AgregaContacto)
                },
                onNavigateToDetail = { contacto ->
                    val ruta = DetalleContacto(
                        nombre = contacto.nombre,
                        telefono = contacto.telefono
                    )
                    navController.navigate(ruta)
                }
            )
        }
        composable<EditaContacto> {
            EditarContactoScreen(
                onClose = {
                }
            )
        }
        composable<AgregaContacto> {
            AgregarContactoScreen()
        }
        composable<DetalleContacto> { backStackEntry ->
            val ruta = backStackEntry.toRoute<DetalleContacto>()
            DetalleContactoScreen(
                fullName = ruta.nombre,
                phone = ruta.telefono,
                initials = ruta.nombre.first().toString(),
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Favoritos> {
            FavoritosScreen()
        }
    }
}