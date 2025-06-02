package com.udb.eventoscomunitarios.data.model

data class EventAttendee(
    val status: String = "registered", // registered, attended, cancelled
    val registrationDate: Long = 0,
    val attendanceDate: Long? = null,
    val notes: String? = null
) {
    constructor() : this("registered", 0, null, null)
}