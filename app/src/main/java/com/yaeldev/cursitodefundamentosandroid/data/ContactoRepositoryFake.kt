package com.yaeldev.cursitodefundamentosandroid.data

class ContactoRepositoryFake : ContactoRepository {

    private val contactos = ContactosMuestra.lista.toMutableList()
    private var siguienteId = 13

    override suspend fun obtenerTodos(): List<Contacto> =
        contactos.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.nombreCompleto })

    override suspend fun obtenerPorId(id: Int): Contacto? =
        contactos.find { it.id == id }

    override suspend fun agregar(contacto: Contacto): Contacto {
        val nuevo = contacto.copy(id = siguienteId++)
        contactos.add(nuevo)
        return nuevo
    }

    override suspend fun actualizar(contacto: Contacto): Contacto {
        val indice = contactos.indexOfFirst { it.id == contacto.id }
        if (indice >= 0) contactos[indice] = contacto
        return contacto
    }

    override suspend fun eliminar(id: Int) {
        contactos.removeAll { it.id == id }
    }

    override suspend fun alternarFavorito(id: Int): Contacto {
        val indice = contactos.indexOfFirst { it.id == id }
        require(indice >= 0) { "No existe un contacto con id $id" }
        val actualizado = contactos[indice].copy(favorite = !contactos[indice].favorite)
        contactos[indice] = actualizado
        return actualizado
    }
}
