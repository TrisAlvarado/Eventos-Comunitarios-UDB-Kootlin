package com.udb.eventoscomunitarios.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.udb.eventoscomunitarios.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun registerWithEmail(
        email: String,
        password: String,
        name: String,
        phone: String? = null
    ): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("Usuario no creado")

            val user = User(
                id = firebaseUser.uid,
                email = email,
                name = name,
                phone = phone,
                profileImageUrl = null,
                emailVerified = firebaseUser.isEmailVerified,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            firestore.collection("users").document(firebaseUser.uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("Usuario no encontrado")

            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
            val user = userDoc.toObject(User::class.java) ?: throw Exception("Datos de usuario no encontrados")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("Usuario no autenticado")

            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java)!!
            } else {
                val newUser = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    name = firebaseUser.displayName ?: "Usuario",
                    profileImageUrl = firebaseUser.photoUrl?.toString(),
                    emailVerified = firebaseUser.isEmailVerified,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                firestore.collection("users").document(firebaseUser.uid).set(newUser).await()
                newUser
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return try {
            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}