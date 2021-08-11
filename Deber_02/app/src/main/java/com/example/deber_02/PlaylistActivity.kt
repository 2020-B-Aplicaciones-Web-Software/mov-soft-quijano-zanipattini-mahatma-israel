package com.example.deber_02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber_02.entities.Playlist
import com.example.deber_02.entities.Song
import com.example.deber_02.recycler_view_adapters.SongsRecyclerViewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import kotlin.math.truncate

class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        // Datos quemados
        val listaCanciones = arrayListOf<Song>()
        llenarDatos(listaCanciones)

        val btnRegresar = findViewById<ImageButton>(R.id.btn_regresar)
        btnRegresar.setOnClickListener{abrirActividad(MainActivity::class.java)}

        // Obtener objeto del Intent Explicito
        val playlist = intent.getParcelableExtra<Playlist>("playlist")

        val descripcionText = findViewById<TextView>(R.id.tv_playlistDescripcion)
        descripcionText.text = playlist?.descripcion

        val imageView = findViewById<ImageView>(R.id.img_playlistCover)
        // Imagen por defecto si recibe null
        val imageName = playlist?.coverName ?: "ic_launcher_background"
        // Obtiene recurso de imagen
        val img = this.resources.getIdentifier(imageName, "drawable", this.packageName)
        imageView.setImageResource(img)

        val duracionPlaylist = findViewById<TextView>(R.id.tv_duracion)
        duracionPlaylist.text = calcularDuracion(listaCanciones)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_songs)
        iniciarRecyclerView(listaCanciones, this, recyclerView)

        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomMenu.itemIconTintList = null
    }

    fun iniciarRecyclerView(
        lista: List<Song>,
        actividad: PlaylistActivity,
        recyclerView: RecyclerView
    ) {
        val adaptador = SongsRecyclerViewAdapter(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad)
        adaptador.notifyDataSetChanged()
    }

    fun abrirActividad(clase: Class<*>){
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    fun calcularDuracion(canciones: List<Song>): String {
        val totalMinutos: Float = canciones
            .map{it.minutos}
            .reduce{acc, min -> return@reduce (acc+min)}
        val horas = truncate(totalMinutos / 60)
        val minutos = ((totalMinutos / 60) - truncate(totalMinutos / 60)) * 60
        return "${horas.toInt()} h ${minutos.toInt()} min"
    }

    // Quemar Datos
    fun llenarDatos(canciones: MutableList<Song>) {
        canciones.add(Song("Signos", "Soda Stereo", 5.27f, "signos"))
        canciones.add(Song("En la ciudad de la furia", "Soda Stereo", 6.27f, "doble_vida"))
        canciones.add(Song("Signos", "Soda Stereo", 5.27f, "signos"))
        canciones.add(Song("En la ciudad de la furia", "Soda Stereo", 6.27f, "doble_vida"))
        canciones.add(Song("Signos", "Soda Stereo", 5.27f, "signos"))
        canciones.add(Song("En la ciudad de la furia", "Soda Stereo", 60.27f, "doble_vida"))
    }


}