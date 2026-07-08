package com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.mappers

import com.yaeldev.cursitodefundamentosandroid.core.security.AesCipher
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.dto.ContactoDocument
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto

fun Contacto.toDocument(): ContactoDocument = ContactoDocument(
    first = first,
    last = last,
    phone = AesCipher.encrypt(phone),
    email = AesCipher.encrypt(email),
    company = AesCipher.encrypt(company),
    favorite = favorite,
    owner = owner
)