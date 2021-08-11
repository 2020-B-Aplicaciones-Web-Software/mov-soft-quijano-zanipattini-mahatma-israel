package com.example.deber_02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.deber_02.entities.Category
import com.example.deber_02.entities.Playlist
import com.example.deber_02.recycler_view_adapters.CategoryRecyclerViewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listaCategorias = arrayListOf<Category>()
        llenarDatos(listaCategorias)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_categories)

        iniciarRecyclerView(listaCategorias, this, recyclerView)

        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomMenu.itemIconTintList = null

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
        listaPlaylists1.add(Playlist("Daily Mix 1", "Callejeros, Tierra Santa, Las Pastillas del Abuelo", "daily_mix_1"))
        listaPlaylists1.add(Playlist("Daily Mix 2", "KISS, Guns N' Roses, Styx y mas", "daily_mix_2"))
        listaPlaylists1.add(Playlist("Daily Mix 3", "Gustavo Cerati, Los Caligaris, Bersuit", "daily_mix_3"))
        listaPlaylists1.add(Playlist("Daily Mix 4", "Verde 70, Cabare, La Maquina Camaleon y mas", "daily_mix_4"))
        categorias.add(Category("Daily Mix", listaPlaylists1))

        val listaPlaylists2 = arrayListOf<Playlist>()
        listaPlaylists2.add(Playlist("Tu capsula del tiempo", "Creamos una playlist personalizada para que", "tu_capsula"))
        listaPlaylists2.add(Playlist("Sin parar", "Tus canciones favoritas del momento", "sin_parar"))
        listaPlaylists2.add(Playlist("Recuerda y repite", "Tus canciones favoritas del pasado", "recuerda_repite"))
        categorias.add(Category("100% tu", listaPlaylists2))

        val listaPlaylists3 = arrayListOf<Playlist>()
        listaPlaylists3.add(Playlist("Radio Latina", "Dale play y llénate de nostalgia con estas canciones inolvidables.", "radio_latina"))
        listaPlaylists3.add(Playlist("Hits Alegres", "¡Tu dosis de energía para alegrarte el día!", "hits_alegres"))
        listaPlaylists3.add(Playlist("Canta en la ducha", "Tu karaoke personal para darlo todo cantando.", "ducha"))
        categorias.add(Category("Estado de animo", listaPlaylists3))
    }
}