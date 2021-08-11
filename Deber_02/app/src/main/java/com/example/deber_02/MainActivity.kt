package com.example.deber_02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listaCategorias = arrayListOf<Category>()
        llenarDatos(listaCategorias)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_categories)

        iniciarRecyclerView(listaCategorias, this, recyclerView)

        // TODO poner la barra de abajo (Home, Buscar, Biblioteca, Premium)
    }

    fun iniciarRecyclerView(
        lista: List<Category>,
        actividad: MainActivity,
        recyclerView: RecyclerView
    ) {
        val adaptador = CategoryRecyclerViewAdapter(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad)
        adaptador.notifyDataSetChanged()
    }

    // Quemar Datos
    fun llenarDatos(categorias: MutableList<Category>) {
        val listaPlaylists1 = arrayListOf<Playlist>()
        listaPlaylists1.add(Playlist("Nombre 1", "Descripcion 1", "history"))
        listaPlaylists1.add(Playlist("Nombre 2", "Descripcion 2", "settings"))
        listaPlaylists1.add(Playlist("Nombre 3", "Descripcion 3", "ic_launcher_foreground"))
        listaPlaylists1.add(Playlist("Nombre 4", "Descripcion 4", null))
        listaPlaylists1.add(Playlist("Nombre 5", "Descripcion 5", "settings"))
        listaPlaylists1.add(Playlist("Nombre 6", "Descripcion 6", "ic_launcher_foreground"))
        categorias.add(Category("Daily Mix", listaPlaylists1))

        val listaPlaylists2 = arrayListOf<Playlist>()
        listaPlaylists2.add(Playlist("Nombre 7", "Descripcion 7", "history"))
        listaPlaylists2.add(Playlist("Nombre 8", "Descripcion 8", "settings"))
        listaPlaylists2.add(Playlist("Nombre 9", "Descripcion 9", "ic_launcher_foreground"))
        listaPlaylists2.add(Playlist("Nombre 10", "Descripcion 10", null))
        listaPlaylists2.add(Playlist("Nombre 11", "Descripcion 11", "settings"))
        listaPlaylists2.add(Playlist("Nombre 12", "Descripcion 12", "ic_launcher_foreground"))
        categorias.add(Category("100% tu", listaPlaylists2))

        val listaPlaylists3 = arrayListOf<Playlist>()
        listaPlaylists3.add(Playlist("Nombre 13", "Descripcion 13", null))
        listaPlaylists3.add(Playlist("Nombre 14", "Descripcion 14", "settings"))
        listaPlaylists3.add(Playlist("Nombre 15", "Descripcion 15", "ic_launcher_foreground"))
        listaPlaylists3.add(Playlist("Nombre 16", "Descripcion 16", null))
        listaPlaylists3.add(Playlist("Nombre 17", "Descripcion 17", "settings"))
        listaPlaylists3.add(Playlist("Nombre 18", "Descripcion 18", "ic_launcher_foreground"))
        categorias.add(Category("Estado de animo", listaPlaylists3))
    }
}