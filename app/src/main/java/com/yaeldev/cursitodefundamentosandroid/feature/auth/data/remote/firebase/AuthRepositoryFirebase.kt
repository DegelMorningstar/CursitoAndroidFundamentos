package com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.core.network.ejecutar
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo
import com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.mappers.toUsuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryFirebase(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = Firebase.firestore
) : AuthRepository {

    private val usuarios get() = db.collection(Catalogo.USUARIOS)

    override suspend fun registrar(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ): Result<Usuario> = ejecutar {
        val resultado = auth.createUserWithEmailAndPassword(email, password).await()
        val usuario = resultado.user
            ?: throw IllegalStateException("No se pudo crear la cuenta")
        // FirebaseAuth solo guarda un displayName: combinamos nombre + apellido.
        val nombreCompleto = listOf(nombre, apellido)
            .filter { it.isNotBlank() }
            .joinToString(" ")
        val perfil = UserProfileChangeRequest.Builder()
            .setDisplayName(nombreCompleto)
            .build()
        usuario.updateProfile(perfil).await()
        val creado = usuario.toUsuario().copy(nombre = nombreCompleto)
        // Publicamos al directorio para que otros lo encuentren por correo.
        guardarEnDirectorio(creado)
        creado
    }

    override suspend fun iniciarSesion(email: String, password: String): Result<Usuario> =
        ejecutar {
            val resultado = auth.signInWithEmailAndPassword(email, password).await()
            val usuario = resultado.user
                ?: throw IllegalStateException("No se pudo iniciar sesion")
            val autenticado = usuario.toUsuario()
            // Upsert al directorio: asegura una entrada aun para cuentas viejas.
            guardarEnDirectorio(autenticado)
            autenticado
        }

    override fun cerrarSesion() {
        auth.signOut()
    }


    /** Crea/actualiza `usuarios/{uid}` con el correo normalizado en minusculas. */
    private suspend fun guardarEnDirectorio(usuario: Usuario) {
        usuarios.document(usuario.id).set(
            mapOf(
                "uid" to usuario.id,
                "nombre" to usuario.nombre,
                Catalogo.CAMPO_EMAIL to usuario.email.lowercase()
            ),
            SetOptions.merge()
        ).await()
    }

}
