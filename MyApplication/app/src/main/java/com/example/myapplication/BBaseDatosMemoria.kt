package com.example.myapplication

class BBaseDatosMemoria {

    companion object {
        // Propiedades
        // Metodos
        // Estaticos (Singleton)
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(
                BEntrenador("Adrian", "a@a.com", null)
            )
            arregloBEntrenador.add(
                BEntrenador("Vicente", "b@b.com", null)
            )
            arregloBEntrenador.add(
                BEntrenador("Carolina", "c@c.com", null)
            )
        }
    }
}