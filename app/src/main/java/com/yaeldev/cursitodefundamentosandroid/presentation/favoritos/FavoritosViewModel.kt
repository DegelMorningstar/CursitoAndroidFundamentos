package com.yaeldev.cursitodefundamentosandroid.presentation.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritosViewModelFactory(
    private val repository: ContactoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            return FavoritosViewModel(repository) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class FavoritosViewModel(val repository: ContactoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    fun getFavoritos() {
        viewModelScope.launch {
            val favoritos = repository.obtenerTodos().filter { it.favorite }
            _uiState.update { state -> state.copy(favoritos = favoritos) }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
            val favoritos = repository.obtenerTodos().filter { it.favorite }
            _uiState.update { state -> state.copy(favoritos = favoritos) }
        }
    }

}
