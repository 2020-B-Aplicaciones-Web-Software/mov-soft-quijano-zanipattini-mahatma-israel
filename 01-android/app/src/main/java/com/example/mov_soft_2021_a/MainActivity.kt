package com.example.mov_soft_2021_a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonIrCicloVida = findViewById<Button>(
            R.id.btn_ir_ciclo_vida
        )
        botonIrCicloVida.setOnClickListener { abrirCicloVida() }
    }

    fun abrirCicloVida() {
        val intentExplicito = Intent(
            this,
            ACicloVida::class.java
        )
        startActivity(intentExplicito)
    }
}