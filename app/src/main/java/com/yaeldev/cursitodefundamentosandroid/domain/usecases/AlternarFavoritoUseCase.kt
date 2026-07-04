package com.yaeldev.cursitodefundamentosandroid.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result

/** Alterna el estado de favorito de un contacto; devuelve el contacto resultante. */
class AlternarFavoritoUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(id: String): Result<Contacto> = repository.alternarFavorito(id)
}
