package com.example.deber_02.recycler_view_adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.deber_02.MainActivity
import com.example.deber_02.PlaylistActivity
import com.example.deber_02.R
import com.example.deber_02.entities.Playlist

class PlaylistRecyclerViewAdapter(
    private val contexto: MainActivity,
    private val listaPlaylists: List<Playlist>,
    private val recyclerView: RecyclerView
) :RecyclerView.Adapter<PlaylistRecyclerViewAdapter.MyViewHolder>() {

    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView: ImageView
        val playlistInfo: TextView

        init {
            imageView = view.findViewById(R.id.img_cover)
            playlistInfo = view.findViewById(R.id.tv_playlistInfo)
        }
    }

    override fun getItemCount(): Int {
        return listaPlaylists.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val playlist = listaPlaylists[position]
        // Imagen por defecto si retgcibe null
        val imageName = playlist.coverName ?: "ic_launcher_background"
        // Obtiene recurso de imagen
        val img = this.contexto.resources.getIdentifier(
            imageName, "drawable", this.contexto.packageName
        )
        holder.imageView.setImageResource(img)
        holder.playlistInfo.text = playlist.toString()
        holder.imageView.setOnClickListener{
            abrirActividadConParametros(PlaylistActivity::class.java, "User", playlist)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.playlist_recycler_view,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    fun abrirActividadConParametros(clase: Class<*>, usuario: String, playlist: Playlist){
        val intentExplicito = Intent(this.contexto, clase)
        intentExplicito.putExtra("user", usuario)
        intentExplicito.putExtra("playlist", playlist)
        ActivityCompat.startActivityForResult(this.contexto, intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO, Bundle.EMPTY)
    }
}