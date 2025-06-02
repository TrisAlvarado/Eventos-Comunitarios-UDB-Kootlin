package com.udb.eventoscomunitarios.data.model

data class Category(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val color: String = "#003366",
    val icon: String = "📚",
    val isActive: Boolean = true
) {
    constructor() : this("", "", "", "#003366", "📚", true)
}