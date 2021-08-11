package com.example.deber_02.recycler_view_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber_02.PlaylistActivity
import com.example.deber_02.R
import com.example.deber_02.entities.Song

class SongsRecyclerViewAdapter(
    private val contexto: PlaylistActivity,
    private val listaCanciones: List<Song>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<SongsRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView: ImageView
        val nombreCancion: TextView
        val artista: TextView

        init {
            imageView = view.findViewById(R.id.img_songCover)
            nombreCancion = view.findViewById(R.id.tv_nombreCancion)
            artista = view.findViewById(R.id.tv_artista)
        }
    }

    override fun getItemCount(): Int {
        return listaCanciones.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cancion = listaCanciones[position]
        holder.nombreCancion.text = cancion.nombre
        holder.artista.text = cancion.artista
        // Imagen por defecto si recibe null
        val imageName = cancion.cover ?: "ic_launcher_background"
        // Obtiene recurso de imagen
        val img = this.contexto.resources.getIdentifier(
            imageName, "drawable", this.contexto.packageName
        )
        holder.imageView.setImageResource(img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.song_recycler_view,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }
}