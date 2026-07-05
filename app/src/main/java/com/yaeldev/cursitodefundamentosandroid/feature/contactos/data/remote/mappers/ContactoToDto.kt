package com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.mappers

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.dto.ContactoDocument
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto

fun Contacto.toDocument(): ContactoDocument = ContactoDocument(
    first = first,
    last = last,
    phone = phone,
    email = email,
    company = company,
    favorite = favorite,
    owner = owner
)