package com.yaeldev.cursitodefundamentosandroid.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.AbrirOCrearChatUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ActualizarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AgregarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.EliminarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactoPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactosUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerFavoritosUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.agregarContacto.AgregarContactoViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.detalleContacto.DetalleContactoViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.editarContacto.EditarContactoViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.favoritos.FavoritosViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.listaContacto.ListaContactoViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories.PerfilRepository
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.BuscarUsuarioPorEmailUseCase

/**
 * DI del feature contactos. Ademas del repositorio propio recibe los repos de
 * perfil y chat (compartidos por la app), porque DetalleContacto resuelve el
 * correo del contacto (perfil) y abre el chat 1:1 (chat).
 */
class ContactosContainer(
    private val contactoRepository: ContactoRepository,
    private val perfilRepository: PerfilRepository,
    private val chatRepository: ChatRepository
) {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ListaContactoViewModel(
                ObtenerContactosUseCase(contactoRepository),
                AlternarFavoritoUseCase(contactoRepository)
            )
        }
        initializer {
            FavoritosViewModel(
                ObtenerFavoritosUseCase(contactoRepository),
                AlternarFavoritoUseCase(contactoRepository)
            )
        }
        initializer { AgregarContactoViewModel(AgregarContactoUseCase(contactoRepository)) }
        initializer {
            DetalleContactoViewModel(
                ObtenerContactoPorIdUseCase(contactoRepository),
                AlternarFavoritoUseCase(contactoRepository),
                EliminarContactoUseCase(contactoRepository),
                BuscarUsuarioPorEmailUseCase(perfilRepository),
                AbrirOCrearChatUseCase(chatRepository)
            )
        }
        initializer {
            EditarContactoViewModel(
                ObtenerContactoPorIdUseCase(contactoRepository),
                ActualizarContactoUseCase(contactoRepository),
                EliminarContactoUseCase(contactoRepository)
            )
        }
    }
}
