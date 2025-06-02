package com.udb.eventoscomunitarios.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.udb.eventoscomunitarios.data.model.Event
import com.udb.eventoscomunitarios.data.model.EventAttendee
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getAllEvents(
        category: String? = null,
        limit: Long = 20
    ): Result<List<Event>> {
        return try {
            var query: Query = firestore.collection("events")
                .whereEqualTo("isActive", true)
                .whereEqualTo("isPublic", true)
                .orderBy("dateTime", Query.Direction.ASCENDING)
                .limit(limit)

            if (category != null) {
                query = query.whereEqualTo("category", category)
            }

            val snapshot = query.get().await()
            val events = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Event::class.java)?.copy(id = doc.id)
            }

            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventById(eventId: String): Result<Event> {
        return try {
            val doc = firestore.collection("events").document(eventId).get().await()
            val event = doc.toObject(Event::class.java)?.copy(id = doc.id)
                ?: throw Exception("Evento no encontrado")

            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createEvent(event: Event): Result<String> {
        return try {
            val docRef = firestore.collection("events").document()
            val eventWithId = event.copy(
                id = docRef.id,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            docRef.set(eventWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerToEvent(eventId: String, userId: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val eventRef = firestore.collection("events").document(eventId)
                val attendeeRef = eventRef.collection("attendees").document(userId)

                val eventSnapshot = transaction.get(eventRef)
                val event = eventSnapshot.toObject(Event::class.java)
                    ?: throw Exception("Evento no encontrado")

                if (event.maxAttendees != null && event.currentAttendees >= event.maxAttendees) {
                    throw Exception("Evento lleno")
                }

                val attendee = EventAttendee(
                    status = "registered",
                    registrationDate = System.currentTimeMillis()
                )

                transaction.set(attendeeRef, attendee)
                transaction.update(eventRef, "currentAttendees", event.currentAttendees + 1)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unregisterFromEvent(eventId: String, userId: String): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val eventRef = firestore.collection("events").document(eventId)
                val attendeeRef = eventRef.collection("attendees").document(userId)

                val eventSnapshot = transaction.get(eventRef)
                val event = eventSnapshot.toObject(Event::class.java)
                    ?: throw Exception("Evento no encontrado")

                transaction.update(attendeeRef, "status", "cancelled")
                transaction.update(eventRef, "currentAttendees", maxOf(0, event.currentAttendees - 1))
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isUserRegistered(eventId: String, userId: String): Result<String?> {
        return try {
            val doc = firestore.collection("events").document(eventId)
                .collection("attendees").document(userId).get().await()

            if (doc.exists()) {
                val attendee = doc.toObject(EventAttendee::class.java)
                Result.success(attendee?.status)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}