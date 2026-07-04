package com.yaeldev.cursitodefundamentosandroid.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result

/** Da de alta un contacto nuevo. */
class AgregarContactoUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(contacto: Contacto): Result<Contacto> = repository.agregar(contacto)
}
