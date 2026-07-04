package com.yaeldev.cursitodefundamentosandroid.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result

/** Obtiene la lista completa de contactos. */
class ObtenerContactosUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(): Result<List<Contacto>> = repository.obtenerTodos()
}
