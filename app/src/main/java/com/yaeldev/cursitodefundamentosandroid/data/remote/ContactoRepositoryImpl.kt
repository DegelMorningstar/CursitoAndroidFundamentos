package com.yaeldev.cursitodefundamentosandroid.data.remote

import com.yaeldev.cursitodefundamentosandroid.data.remote.mappers.fromUserDTOListToContactoDomain
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result
import com.yaeldev.cursitodefundamentosandroid.util.toMessage
import kotlin.coroutines.cancellation.CancellationException

class ContactoRepositoryImpl(
    private val api: RandomUserAPI
) : ContactoRepository {

    override suspend fun obtenerTodos(): Result<List<Contacto>> {
        return try {
            val userResponseDTO = api.obtenerContactos()
            val newList = fromUserDTOListToContactoDomain(userResponseDTO.results)
            Result.Success(data = newList)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.Error(e.toMessage())
        }
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