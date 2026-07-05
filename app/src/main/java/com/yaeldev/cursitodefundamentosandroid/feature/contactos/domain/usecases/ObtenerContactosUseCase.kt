package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Obtiene la lista completa de contactos. */
class ObtenerContactosUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(): Result<List<Contacto>> = repository.obtenerTodos()
}
