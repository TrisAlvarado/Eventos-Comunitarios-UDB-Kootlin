package com.udb.eventoscomunitarios.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val isActive: Boolean = true,
    val emailVerified: Boolean = false,
    val createdAt: Long = 0,
    val updatedAt: Long = 0
) {
    constructor() : this("", "", "")
}