package com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.dto

data class ContactoDocument(
    val first: String = "",
    val last: String = "",
    val phone: String = "",
    val email: String = "",
    val company: String = "",
    val favorite: Boolean = false,
    val owner: String = ""
)