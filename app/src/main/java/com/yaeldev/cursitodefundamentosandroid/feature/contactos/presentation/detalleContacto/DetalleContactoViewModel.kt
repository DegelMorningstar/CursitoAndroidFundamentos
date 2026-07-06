package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.detalleContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.EliminarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactoPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.BuscarUsuarioPorEmailUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.AbrirOCrearChatUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleContactoViewModelFactory(
    private val obtenerContactoPorId: ObtenerContactoPorIdUseCase,
    private val alternarFavorito: AlternarFavoritoUseCase,
    private val eliminarContacto: EliminarContactoUseCase,
    private val buscarUsuarioPorEmail: BuscarUsuarioPorEmailUseCase,
    private val abrirOCrearChat: AbrirOCrearChatUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleContactoViewModel::class.java)) {
            return DetalleContactoViewModel(
                obtenerContactoPorId,
                alternarFavorito,
                eliminarContacto,
                buscarUsuarioPorEmail,
                abrirOCrearChat
            ) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class DetalleContactoViewModel(
    private val obtenerContactoPorId: ObtenerContactoPorIdUseCase,
    private val alternarFavoritoUseCase: AlternarFavoritoUseCase,
    private val eliminarContacto: EliminarContactoUseCase,
    private val buscarUsuarioPorEmail: BuscarUsuarioPorEmailUseCase,
    private val abrirOCrearChat: AbrirOCrearChatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleContactoUiState>(DetalleContactoUiState.Loading)
    val uiState: StateFlow<DetalleContactoUiState> = _uiState.asStateFlow()

    private var contactoId: String = ""

    fun cargarContacto(id: String) {
        contactoId = id
        viewModelScope.launch {
            _uiState.update { DetalleContactoUiState.Loading }
            _uiState.update {
                when (val resultado = obtenerContactoPorId(id)) {
                    is Result.Success -> {
                        val contacto = resultado.data ?: return@update DetalleContactoUiState.Empty
                        DetalleContactoUiState.Success(
                            fullName = contacto.nombreCompleto,
                            initials = contacto.iniciales,
                            company = contacto.company,
                            phone = contacto.phone,
                            email = contacto.email,
                            esFavorito = contacto.favorite
                        )
                    }
                    is Result.Error -> DetalleContactoUiState.Error(resultado.message)
                }
            }
        }
    }

    fun alternarFavorito() {
        val actual = _uiState.value as? DetalleContactoUiState.Success ?: return
        viewModelScope.launch {
            _uiState.update {
                when (val resultado = alternarFavoritoUseCase(contactoId)) {
                    is Result.Success -> actual.copy(esFavorito = resultado.data.favorite, error = null)
                    is Result.Error -> actual.copy(error = resultado.message)
                }
            }
        }
    }

    fun eliminar(onEliminado: () -> Unit) {
        val actual = _uiState.value as? DetalleContactoUiState.Success ?: return
        viewModelScope.launch {
            when (val resultado = eliminarContacto(contactoId)) {
                is Result.Success -> onEliminado()
                is Result.Error -> _uiState.update { actual.copy(error = resultado.message) }
            }
        }
    }

    /**
     * Resuelve el correo del contacto contra el directorio de usuarios; si es un
     * usuario de la app, abre/crea el chat y navega. Si no, ofrece chatear por una
     * mensajeria externa (SMS/WhatsApp/Telegram) mostrando las opciones.
     */
    fun abrirChat(onChatListo: (chatId: String, titulo: String, otroUid: String) -> Unit) {
        val actual = _uiState.value as? DetalleContactoUiState.Success ?: return
        // Sin correo no se puede resolver contra el directorio; si hay telefono,
        // ofrecemos directamente la mensajeria externa.
        if (actual.email.isBlank()) {
            _uiState.update {
                if (actual.phone.isBlank()) {
                    actual.copy(error = "Este contacto no tiene correo ni telefono para chatear.")
                } else {
                    actual.copy(error = null, mostrarOpcionesExternas = true)
                }
            }
            return
        }
        viewModelScope.launch {
            when (val busqueda = buscarUsuarioPorEmail(actual.email)) {
                is Result.Error -> _uiState.update { actual.copy(error = busqueda.message) }
                is Result.Success -> {
                    val usuario = busqueda.data
                    if (usuario == null) {
                        // No es usuario de la app: ofrecer SMS/WhatsApp/Telegram si hay telefono.
                        _uiState.update {
                            if (actual.phone.isBlank()) {
                                actual.copy(error = "Este contacto aun no es usuario de la app y no tiene telefono.")
                            } else {
                                actual.copy(error = null, mostrarOpcionesExternas = true)
                            }
                        }
                        return@launch
                    }
                    when (val chat = abrirOCrearChat(usuario.id, usuario.nombre)) {
                        is Result.Success -> onChatListo(chat.data, actual.fullName, usuario.id)
                        is Result.Error -> _uiState.update { actual.copy(error = chat.message) }
                    }
                }
            }
        }
    }

    /** Cierra la hoja de opciones de mensajeria externa. */
    fun cerrarOpcionesExternas() {
        val actual = _uiState.value as? DetalleContactoUiState.Success ?: return
        _uiState.update { actual.copy(mostrarOpcionesExternas = false) }
    }

}
