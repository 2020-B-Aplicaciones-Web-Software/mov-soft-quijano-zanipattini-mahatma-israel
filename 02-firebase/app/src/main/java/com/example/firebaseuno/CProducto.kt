package com.example.firebaseuno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CProducto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cproducto)

        val botonCrear = findViewById<Button>(R.id.btn_crear_producto)
        botonCrear.setOnClickListener {
            crearProducto()
        }
    }

    fun crearProducto() {
        val textNombre = findViewById<EditText>(R.id.et_nombre_producto)
        val textPrecio = findViewById<EditText>(R.id.et_precio_producto)

        val nuevoProducto = hashMapOf<String, Any>(
            "nombre" to textNombre.text.toString(),
            "precio" to textPrecio.text.toString().toDouble()
        )

        val db = Firebase.firestore
        val referencia = db.collection("producto")

        referencia
            .add(nuevoProducto)
            .addOnSuccessListener{
                textNombre.text.clear()
                textPrecio.text.clear()
            }
            .addOnFailureListener{}
    }
}