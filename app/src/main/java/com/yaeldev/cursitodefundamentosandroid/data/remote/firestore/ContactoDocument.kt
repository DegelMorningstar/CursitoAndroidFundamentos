package com.yaeldev.cursitodefundamentosandroid.data.remote.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto

/**
 * Representacion del contacto tal como se guarda en Firestore: solo los campos
 * persistidos. NO incluye el id (ese es el id del documento) ni las propiedades
 * calculadas del dominio (nombreCompleto, iniciales).
 *
 * Todos los campos llevan valor por defecto porque Firestore necesita un
 * constructor sin argumentos para deserializar con toObject().
 */
data class ContactoDocument(
    val first: String = "",
    val last: String = "",
    val phone: String = "",
    val email: String = "",
    val company: String = "",
    val favorite: Boolean = false
)

/** Dominio -> documento Firestore (descarta el id). */
fun Contacto.toDocument(): ContactoDocument = ContactoDocument(
    first = first,
    last = last,
    phone = phone,
    email = email,
    company = company,
    favorite = favorite
)

/**
 * Documento Firestore -> dominio, combinando el id del documento con los campos.
 * Devuelve null si el documento no existe o no se puede deserializar.
 */
fun DocumentSnapshot.toContacto(): Contacto? {
    val doc = toObject(ContactoDocument::class.java) ?: return null
    return Contacto(
        id = id,
        first = doc.first,
        last = doc.last,
        phone = doc.phone,
        email = doc.email,
        company = doc.company,
        favorite = doc.favorite
    )
}
