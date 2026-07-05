package com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.core.network.ejecutar
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.CAMPO_EMAIL
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.USUARIOS
import com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.mappers.toUsuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.mappers.toUsuarioDirectorio
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories.PerfilRepository
import kotlinx.coroutines.tasks.await

class PerfilRepositoryFirebase(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = Firebase.firestore
): PerfilRepository {

    private val usuarios get() = db.collection(USUARIOS)

    override fun usuarioActual(): Usuario? = auth.currentUser?.toUsuario()

    override suspend fun buscarPorEmail(email: String): Result<Usuario?> = ejecutar {
        val snapshot = usuarios
            .whereEqualTo(CAMPO_EMAIL, email.trim().lowercase())
            .limit(1)
            .get()
            .await()
        snapshot.documents.firstOrNull()?.toUsuarioDirectorio()
    }

    override suspend fun obtenerPerfil(): Result<Usuario?> = ejecutar {
        val uid = auth.currentUser?.uid ?: return@ejecutar null
        val doc = usuarios.document(uid).get().await()
        // Si aun no hay doc de directorio, caemos a lo basico de FirebaseAuth.
        doc.takeIf { it.exists() }?.toUsuarioDirectorio() ?: auth.currentUser?.toUsuario()
    }

    override suspend fun obtenerUsuarioPorId(uid: String): Result<Usuario?> = ejecutar {
        val doc = usuarios.document(uid).get().await()
        doc.takeIf { it.exists() }?.toUsuarioDirectorio()
    }

    override suspend fun actualizarPerfil(
        nombre: String,
        foto: String,
        estado: EstadoUsuario
    ): Result<Usuario> = ejecutar {
        val user = auth.currentUser ?: throw IllegalStateException("No hay sesion activa")
        // El nombre se refleja en el displayName de FirebaseAuth y en el directorio.
        user.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(nombre).build()
        ).await()
        usuarios.document(user.uid).set(
            mapOf(
                "uid" to user.uid,
                "nombre" to nombre,
                CAMPO_EMAIL to user.email.orEmpty().lowercase(),
                "foto" to foto,
                "estado" to estado.name
            ),
            SetOptions.merge()
        ).await()
        Usuario(
            id = user.uid,
            email = user.email.orEmpty(),
            nombre = nombre,
            foto = foto,
            estado = estado
        )
    }

}