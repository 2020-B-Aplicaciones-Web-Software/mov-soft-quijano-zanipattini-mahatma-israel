package com.example.firebaseuno.entities

class Restaurante(
    val nombre: String,
    val uid: String
) {
    var calificacionPromedio: Float = 0f
    var sumatoriaCalificaciones: Int = 0
    var usuariosCalificados: Int = 0

    fun actualizarCalificacion(nuevaCalificacion: Int) {
        this.sumatoriaCalificaciones += nuevaCalificacion
        this.usuariosCalificados += 1
        this.calificacionPromedio = this.sumatoriaCalificaciones.toFloat() / this.usuariosCalificados.toFloat()
    }

    override fun toString(): String {
        return nombre
    }

    override fun equals(other: Any?): Boolean {
        //return super.equals(other)
        return if (other is Restaurante)
            this.nombre.equals(other.nombre)
        else
            false
    }
}