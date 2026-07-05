package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerFavoritosUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val obtenerFavoritos: ObtenerFavoritosUseCase,
    private val alternarFavoritoUseCase: AlternarFavoritoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritosUiState>(FavoritosUiState.Loading)
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    fun getFavoritos() {
        viewModelScope.launch {
            _uiState.update { FavoritosUiState.Loading }
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
        _uiState.update {
            when (val resultado = obtenerFavoritos()) {
                is Result.Success ->
                    if (resultado.data.isEmpty()) FavoritosUiState.Empty
                    else FavoritosUiState.Success(resultado.data)
                is Result.Error -> FavoritosUiState.Error(resultado.message)
            }
        }
    }

}
