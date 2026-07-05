package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Obtiene un contacto por su id. Success(null) si no existe. */
class ObtenerContactoPorIdUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(id: String): Result<Contacto?> = repository.obtenerPorId(id)
}
