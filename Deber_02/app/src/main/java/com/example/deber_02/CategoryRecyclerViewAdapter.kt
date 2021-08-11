package com.example.deber_02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryRecyclerViewAdapter(
    private val contexto: MainActivity,
    private val listaCategorias: List<Category>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tituloText: TextView
        val playlistsRecyclerView: RecyclerView

        init {
            tituloText = view.findViewById(R.id.tv_tituloCategoria)
            playlistsRecyclerView = view.findViewById(R.id.rv_playlists)
        }
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = listaCategorias[position]
        holder.tituloText.text = category.titulo
        // Llenar el RecyclerView
        iniciarRecyclerView(category.listas, contexto, holder.playlistsRecyclerView)
    }

    fun iniciarRecyclerView(
        lista: List<Playlist>,
        actividad: MainActivity,
        recyclerView: RecyclerView
    ) {
        val adaptador = PlaylistRecyclerViewAdapter(
            actividad,
            lista,
            recyclerView
        )
        // Horizontal
        recyclerView.layoutManager = LinearLayoutManager(actividad, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        adaptador.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.category_recycler_view,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }
}