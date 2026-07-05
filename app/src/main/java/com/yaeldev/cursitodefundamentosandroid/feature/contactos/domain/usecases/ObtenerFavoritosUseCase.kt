package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/**
 * Obtiene solo los contactos marcados como favoritos. El filtrado es logica de
 * dominio, por eso vive aqui y no en el ViewModel.
 */
class ObtenerFavoritosUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(): Result<List<Contacto>> =
        when (val resultado = repository.obtenerTodos()) {
            is Result.Success -> Result.Success(resultado.data.filter { it.favorite })
            is Result.Error -> resultado
        }
}
