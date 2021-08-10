package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ACicloVida : AppCompatActivity() {

    var numero = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
        Log.i("ciclo-vida", "onCreate")
        val botonCicloVida = findViewById<Button>(
            R.id.btn_aumentar
        )
        actualizarNumeroEnPantalla()

        botonCicloVida.setOnClickListener{
            aumentarNumero()
            actualizarNumeroEnPantalla()
        }
    }

    fun aumentarNumero() {
        numero = numero + 1
    }

    fun actualizarNumeroEnPantalla() {
        val textViewCicloVida = findViewById<TextView>(
            R.id.txv_ciclo_vida
        )
        textViewCicloVida.text = numero.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            // Aqui se almacena cualquier primitivo
            putInt("numeroGuardado", numero)
        }
        Log.i("ciclo-vida", "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val numeroRecuperado: Int? = savedInstanceState.getInt("numeroGuardado")
        if (numeroRecuperado != null) {
            Log.i("ciclo-vida", "Llego numero ${numeroRecuperado}")
            numero = numeroRecuperado
            actualizarNumeroEnPantalla()
        }
        Log.i("ciclo-vida", "onRestoreInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.i("ciclo-vida", "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("ciclo-vida", "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("ciclo-vida", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ciclo-vida", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("ciclo-vida", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ciclo-vida", "onDestroy")
    }
}