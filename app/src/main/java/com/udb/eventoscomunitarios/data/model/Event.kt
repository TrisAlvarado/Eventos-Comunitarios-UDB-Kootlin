package com.udb.eventoscomunitarios.data.model

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: Long = 0,
    val endDateTime: Long? = null,
    val location: String = "",
    val address: String? = null,
    val imageUrl: String? = null,
    val category: String = "",
    val organizerId: String = "",
    val organizerName: String = "",
    val maxAttendees: Int? = null,
    val currentAttendees: Int = 0,
    val isActive: Boolean = true,
    val isPublic: Boolean = true,
    val registrationDeadline: Long? = null,
    val ccLicense: String = "CC BY-NC 4.0",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
) {
    constructor() : this("", "", "", 0, null, "", null, null, "", "", "", null, 0, true, true, null, "CC BY-NC 4.0", 0, 0)
}