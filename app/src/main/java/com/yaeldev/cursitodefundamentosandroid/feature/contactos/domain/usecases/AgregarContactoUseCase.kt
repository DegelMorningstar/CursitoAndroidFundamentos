package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Da de alta un contacto nuevo. */
class AgregarContactoUseCase(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke(contacto: Contacto): Result<Contacto> = repository.agregar(contacto)
}
