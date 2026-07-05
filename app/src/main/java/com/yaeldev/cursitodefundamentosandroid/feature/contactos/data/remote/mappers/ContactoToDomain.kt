package com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.dto.ContactoDocument
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto

fun DocumentSnapshot.toContacto(): Contacto? {
    val doc = toObject(ContactoDocument::class.java) ?: return null
    return Contacto(
        id = id,
        first = doc.first,
        last = doc.last,
        phone = doc.phone,
        email = doc.email,
        company = doc.company,
        favorite = doc.favorite,
        owner = doc.owner
    )
}
