package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Alterna el estado de favorito de un contacto; devuelve el contacto resultante. */
class AlternarFavoritoUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(id: String): Result<Contacto> = repository.alternarFavorito(id)
}
