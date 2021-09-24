package com.example.examen02.view.departamento

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.examen02.R
import com.example.examen02.entities.Departamento
import com.example.examen02.entities.Edificio
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaDepartamento : AppCompatActivity() {

    // Intent
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401
    lateinit var departamento: Departamento
    lateinit var edificio: Edificio
    // Mapa
    private lateinit var mapa: GoogleMap
    var permisos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_departamento)
        solicitarPermisos()

        // Intent
        departamento = intent.getParcelableExtra("departamento")!!
        edificio = intent.getParcelableExtra("edificio")!!

        // Nombre departamento
        val txtDepartamento = findViewById<TextView>(R.id.tv_nombreDepartamento)
        txtDepartamento.text = departamento.nombre

        // Mapa
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            if (googleMap != null) {
                mapa = googleMap
                configurarMapa()
                val posicion = LatLng(
                    departamento.latitud.toDouble(),
                    departamento.longitud.toDouble()
                )
                agregarMarcador(posicion, departamento.nombre!!)
                centrarMapa(posicion)
            }
        }

        // Boton centrar mapa
        val btnCentrarMapa = findViewById<Button>(R.id.btn_centrarMapa)
        btnCentrarMapa.setOnClickListener {
            centrarMapa(
                LatLng(
                    departamento.latitud.toDouble(),
                    departamento.longitud.toDouble()
                )
            )
        }

        // Boton regresar
        val btnRegresar = findViewById<Button>(R.id.btn_regresarAListarDepartamentos)
        btnRegresar.setOnClickListener {
            abrirActividadConParametros(ListarDepartamentos::class.java, edificio)
        }

    }

    fun centrarMapa(posicion: LatLng, zoom: Float = 15f) {
        mapa.moveCamera(
            CameraUpdateFactory.newLatLngZoom(posicion, zoom)
        )
    }

    fun agregarMarcador(posicion: LatLng, etiqueta: String) {
        mapa.addMarker(
            MarkerOptions()
                .position(posicion)
                .title(etiqueta)
        )
    }

    fun configurarMapa() {
        val contexto = this.applicationContext
        with(mapa) {
            val permisosFineLocation = ContextCompat
                .checkSelfPermission(
                    contexto,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
            if (tienePermisos) {
                mapa.isMyLocationEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
    }

    fun solicitarPermisos() {
        val contexto = this.applicationContext
        val permisosFineLocation = ContextCompat
            .checkSelfPermission(
                contexto,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
        if (tienePermisos) {
            permisos = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }
    }

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }
}