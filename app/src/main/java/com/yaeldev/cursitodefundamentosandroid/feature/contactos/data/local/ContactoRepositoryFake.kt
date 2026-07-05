package com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.local

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

class ContactoRepositoryFake : ContactoRepository {

    private val contactos = ContactosMuestra.lista.toMutableList()
    private var siguienteId = 13

    override suspend fun obtenerTodos(): Result<List<Contacto>> =
        Result.Success(data = contactos.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.nombreCompleto }))

    override suspend fun obtenerPorId(id: String): Result<Contacto?> =
        Result.Success(data = contactos.find { it.id == id })

    override suspend fun agregar(contacto: Contacto): Result<Contacto> {
        val nuevo = contacto.copy(id = (siguienteId++).toString())
        contactos.add(nuevo)
        return Result.Success(data = nuevo)
    }

    override suspend fun actualizar(contacto: Contacto): Result<Contacto> {
        val indice = contactos.indexOfFirst { it.id == contacto.id }
        if (indice >= 0) contactos[indice] = contacto
        return Result.Success(data = contacto)
    }

    override suspend fun eliminar(id: String): Result<Unit> {
        contactos.removeAll { it.id == id }
        return Result.Success(data = Unit)
    }

    override suspend fun alternarFavorito(id: String): Result<Contacto> {
        val indice = contactos.indexOfFirst { it.id == id }
        if (indice < 0) return Result.Error("No existe un contacto con id $id")
        val actualizado = contactos[indice].copy(favorite = !contactos[indice].favorite)
        contactos[indice] = actualizado
        return Result.Success(data = actualizado)
    }
}
