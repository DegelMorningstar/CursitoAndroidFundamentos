package com.yaeldev.cursitodefundamentosandroid.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result

/** Elimina un contacto por su id. */
class EliminarContactoUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.eliminar(id)
}
