package com.udb.eventoscomunitarios.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.udb.eventoscomunitarios.data.model.Category
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val snapshot = firestore.collection("categories")
                .whereEqualTo("isActive", true)
                .orderBy("name")
                .get().await()

            val categories = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Category::class.java)?.copy(id = doc.id)
            }

            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun initializeCategories() {
        val defaultCategories = listOf(
            Category("", "Académico", "Charlas, conferencias y actividades académicas", "#003366", "📚"),
            Category("", "Cultural", "Eventos culturales, festivales y arte", "#FF6B35", "🎭"),
            Category("", "Deportes", "Actividades deportivas y competencias", "#4CAF50", "⚽"),
            Category("", "Social", "Eventos sociales y de networking", "#9C27B0", "🤝"),
            Category("", "Taller", "Talleres prácticos y workshops", "#FF9800", "🔧"),
            Category("", "Conferencia", "Conferencias y seminarios especializados", "#2196F3", "🎤")
        )

        try {
            defaultCategories.forEach { category ->
                firestore.collection("categories").add(category).await()
            }
        } catch (e: Exception) {
            // Categorías ya existen o error - no hacer nada
        }
    }
}