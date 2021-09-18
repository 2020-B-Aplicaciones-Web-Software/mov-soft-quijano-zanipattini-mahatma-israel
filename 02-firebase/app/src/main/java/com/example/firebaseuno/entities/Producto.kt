package com.example.firebaseuno.entities

class Producto(
    val nombre: String,
    val precio: Float,
    val cantidad: Int,
    val uid: String
) {
    override fun toString(): String {
        return "${nombre} ---------- $${precio * cantidad}\n" +
               "\tCantidad: ${cantidad}\n" +
               "\tPrecio unitario: $${precio}"
    }
}