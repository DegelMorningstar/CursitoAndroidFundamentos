package com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.firestore

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.core.network.ejecutar
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.CAMPO_OWNER
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.COLECCION
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.mappers.toContacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.mappers.toDocument
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import kotlinx.coroutines.tasks.await

class ContactoRepositoryFirestore(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ContactoRepository {

    private val coleccion get() = db.collection(COLECCION)

    override suspend fun obtenerTodos(): Result<List<Contacto>> = ejecutar {
        coleccion.whereEqualTo(CAMPO_OWNER, uidActual()).get().await().documents
            .mapNotNull { it.toContacto() }
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.nombreCompleto })
    }

    override suspend fun obtenerPorId(id: String): Result<Contacto?> = ejecutar {
        coleccion.document(id).get().await().toContacto()
    }

    override suspend fun agregar(contacto: Contacto): Result<Contacto> = ejecutar {
        // Sellamos el dueño antes de escribir; add() genera el id del documento.
        val conDueno = contacto.copy(owner = uidActual())
        val referencia = coleccion.add(conDueno.toDocument()).await()
        conDueno.copy(id = referencia.id)
    }

    override suspend fun actualizar(contacto: Contacto): Result<Contacto> = ejecutar {
        // Re-sellamos el dueño para que un update no pueda cambiar la propiedad.
        val conDueno = contacto.copy(owner = uidActual())
        coleccion.document(conDueno.id).set(conDueno.toDocument()).await()
        conDueno
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

    /** uid del usuario con sesion activa; falla si nadie ha iniciado sesion. */
    private fun uidActual(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("No hay sesion activa")

}
