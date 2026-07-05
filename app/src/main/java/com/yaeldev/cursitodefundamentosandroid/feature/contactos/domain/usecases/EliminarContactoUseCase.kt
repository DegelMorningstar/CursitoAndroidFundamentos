package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Elimina un contacto por su id. */
class EliminarContactoUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.eliminar(id)
}
