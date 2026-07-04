package com.yaeldev.cursitodefundamentosandroid.presentation.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.domain.usecases.ObtenerFavoritosUseCase
import com.yaeldev.cursitodefundamentosandroid.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritosViewModelFactory(
    private val obtenerFavoritos: ObtenerFavoritosUseCase,
    private val alternarFavorito: AlternarFavoritoUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            return FavoritosViewModel(obtenerFavoritos, alternarFavorito) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class FavoritosViewModel(
    private val obtenerFavoritos: ObtenerFavoritosUseCase,
    private val alternarFavoritoUseCase: AlternarFavoritoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    fun getFavoritos() {
        viewModelScope.launch {
            cargarFavoritos()
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch {
            // Alternamos y recargamos: si se quito el favorito debe desaparecer.
            alternarFavoritoUseCase(id)
            cargarFavoritos()
        }
    }

    private suspend fun cargarFavoritos() {
        when (val resultado = obtenerFavoritos()) {
            is Result.Success -> _uiState.update { it.copy(favoritos = resultado.data) }
            is Result.Error -> _uiState.update { it.copy(favoritos = emptyList()) }
        }
    }

}
