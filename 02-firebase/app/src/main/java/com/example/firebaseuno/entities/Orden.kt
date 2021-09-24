package com.example.firebaseuno.entities

import java.text.SimpleDateFormat

class Orden(
    val fechaPedido: String,
    val total: Float,
    val estado: String,
    val usuario: String,
    val productos: ArrayList<Producto>
) {
    var restaurante: Restaurante? = null

    companion object {
        // Estados
        val POR_RECIBIR = "Por recibir"
        val PREPARANDO = "Preparando"
        val ENVIADO = "Enviado"
        val ENTREGADO = "Entregado"
        val CANCELADO = "Cancelado"
        val ESTADOS = arrayOf(POR_RECIBIR, PREPARANDO, ENVIADO, ENTREGADO, CANCELADO)

        // Calificaciones
        val MUY_BUENO = 3
        val REGULAR = 2
        val MALO = 1
        val CALIFICACIONES = arrayOf(MUY_BUENO, REGULAR, MALO)
    }

    override fun toString(): String {
        var stringProductos = ""
        productos.forEach {
            stringProductos += "${it.nombre}: ${it.cantidad}\n"
        }
        var format = SimpleDateFormat("EEE MMM dd hh:mm:ss Z yyyy")
        val date = format.parse(fechaPedido)
        format = SimpleDateFormat("dd/MM/yyyy hh:mm a")

        return "----- ${format.format(date)} ---------------\n" +
               stringProductos +
               "----- Total a pagar: $$total"
    }
}