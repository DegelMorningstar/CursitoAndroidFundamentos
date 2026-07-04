package com.yaeldev.cursitodefundamentosandroid.data.remote.firestore

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result
import com.yaeldev.cursitodefundamentosandroid.util.toMessage
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

/**
 * Implementacion del repositorio contra Cloud Firestore. La coleccion "contactos"
 * es la unica fuente de verdad del CRUD. El id del dominio es el id del documento.
 *
 * Todas las operaciones devuelven util.Result: nunca lanzan al llamador (salvo
 * CancellationException, que se re-lanza para respetar la cancelacion de corrutinas).
 */
class ContactoRepositoryFirestore(
    private val db: FirebaseFirestore = Firebase.firestore
) : ContactoRepository {

    private val coleccion get() = db.collection(COLECCION)

    override suspend fun obtenerTodos(): Result<List<Contacto>> = ejecutar {
        coleccion.get().await().documents
            .mapNotNull { it.toContacto() }
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.nombreCompleto })
    }

    override suspend fun obtenerPorId(id: String): Result<Contacto?> = ejecutar {
        coleccion.document(id).get().await().toContacto()
    }

    override suspend fun agregar(contacto: Contacto): Result<Contacto> = ejecutar {
        // add() genera el id del documento; lo devolvemos en el contacto resultante.
        val referencia = coleccion.add(contacto.toDocument()).await()
        contacto.copy(id = referencia.id)
    }

    override suspend fun actualizar(contacto: Contacto): Result<Contacto> = ejecutar {
        coleccion.document(contacto.id).set(contacto.toDocument()).await()
        contacto
    }

    override suspend fun eliminar(id: String): Result<Unit> = ejecutar {
        coleccion.document(id).delete().await()
        Unit
    }

    override suspend fun alternarFavorito(id: String): Result<Contacto> = ejecutar {
        val actual = coleccion.document(id).get().await().toContacto()
            ?: throw NoSuchElementException("No existe un contacto con id $id")
        val nuevoValor = !actual.favorite
        coleccion.document(id).update("favorite", nuevoValor).await()
        actual.copy(favorite = nuevoValor)
    }

    /**
     * Ejecuta [bloque] envolviendo el resultado en Result. Re-lanza
     * CancellationException; cualquier otro error se traduce a Result.Error.
     */
    private inline fun <T> ejecutar(bloque: () -> T): Result<T> {
        return try {
            Result.Success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.Error(e.toMessage())
        }
    }

    companion object {
        private const val COLECCION = "contactos"
    }
}
