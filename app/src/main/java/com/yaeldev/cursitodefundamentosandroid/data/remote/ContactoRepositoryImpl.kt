package com.yaeldev.cursitodefundamentosandroid.data.remote

import com.yaeldev.cursitodefundamentosandroid.data.remote.mappers.fromUserDTOListToContactoDomain
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository

class ContactoRepositoryImpl(
    private val api: RandomUserAPI
) : ContactoRepository {

    override suspend fun obtenerTodos(): List<Contacto> {
        val response = api.obtenerContactos()
        val newList = fromUserDTOListToContactoDomain(response.results)
        return newList
    }

    override suspend fun obtenerPorId(id: Int): Contacto? {
        TODO("Not yet implemented")
    }

    override suspend fun agregar(contacto: Contacto): Contacto {
        TODO("Not yet implemented")
    }

    override suspend fun actualizar(contacto: Contacto): Contacto {
        TODO("Not yet implemented")
    }

    override suspend fun eliminar(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun alternarFavorito(id: Int): Contacto {
        TODO("Not yet implemented")
    }
}