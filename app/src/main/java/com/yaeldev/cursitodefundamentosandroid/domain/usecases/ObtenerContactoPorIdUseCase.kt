package com.yaeldev.cursitodefundamentosandroid.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result

/** Obtiene un contacto por su id. Success(null) si no existe. */
class ObtenerContactoPorIdUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(id: String): Result<Contacto?> = repository.obtenerPorId(id)
}
