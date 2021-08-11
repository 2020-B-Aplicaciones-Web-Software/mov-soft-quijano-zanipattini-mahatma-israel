package com.example.deber_02

class Category(
    val titulo: String?,
    val listas: List<Playlist>
){
    override fun toString(): String {
        return titulo ?: "Sin titulo"
    }
}