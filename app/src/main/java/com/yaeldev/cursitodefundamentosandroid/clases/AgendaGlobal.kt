package com.yaeldev.cursitodefundamentosandroid.clases

object AgendaGlobal : RepositorioContactos {

    private val contactos = mutableListOf<Contacto>()

    override fun agregar(contacto: Contacto): ResultadoAgenda {
        if (contactos.contains(contacto)) {
            return ResultadoAgenda.Error("Ya existe un contacto con el nombre ${contacto.nombre}")
        }
        contactos.add(contacto)
        return ResultadoAgenda.Exito("Contacto ${contacto.nombre} agregado correctamente")
    }

    override fun obtener(nombre: String): Contacto? {
        return contactos.find { nombre == it.nombre }
    }

    override fun eliminar(contacto: Contacto): ResultadoAgenda {
        return if (contactos.remove(contacto)) {
            ResultadoAgenda.Exito("Contacto ${contacto.nombre} eliminado")
        } else {
            ResultadoAgenda.Error("El contacto ${contacto.nombre} no estaba en la agenda")
        }
    }

    override fun total(): Int = contactos.size

    override fun nombres(): List<String> = contactos.map { it.nombre }

    // Demuestra el polimorfismo: cada contacto resuelve su propio describir().
    override fun describirTodos(): List<String> = contactos.map { it.describir() }
}
