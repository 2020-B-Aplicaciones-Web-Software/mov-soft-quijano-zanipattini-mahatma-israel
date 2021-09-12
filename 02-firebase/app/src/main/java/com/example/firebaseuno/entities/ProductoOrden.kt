package com.example.firebaseuno.entities

class ProductoOrden(
    val restaurante: String,
    val producto: String,
    val precioUnitario: Float,
    val cantidad: Int,
) {

    override fun toString(): String {
        return "${producto} ---------- $${precioUnitario * cantidad}\n" +
               "\tCantidad: ${cantidad}\n" +
               "\t${restaurante}\n" +
               "\tPrecio unitario: $${precioUnitario}"
    }
}