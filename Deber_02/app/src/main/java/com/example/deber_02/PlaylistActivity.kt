package com.example.deber_02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val btnRegresar = findViewById<ImageButton>(R.id.btn_regresar)
        btnRegresar.setOnClickListener{abrirActividad(MainActivity::class.java)}

        // Obtener objeto del Intent Explicito
        val playlist = intent.getParcelableExtra<Playlist>("playlist")

        val descripcionText = findViewById<TextView>(R.id.tv_playlistDescripcion)
        descripcionText.text = playlist?.descripcion

        val imageView = findViewById<ImageView>(R.id.img_playlistCover)
        // Imagen por defecto si recibe null
        val imageName = if (playlist?.coverName == null)
            "ic_launcher_background" else playlist?.coverName
        // Obtiene recurso de imagen
        val img = this.resources.getIdentifier(
            imageName, "drawable", this.packageName
        )
        imageView.setImageResource(img)

    }

    fun abrirActividad(clase: Class<*>){
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }


}