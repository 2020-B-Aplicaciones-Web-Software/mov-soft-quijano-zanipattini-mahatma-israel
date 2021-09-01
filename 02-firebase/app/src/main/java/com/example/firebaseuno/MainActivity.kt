package com.example.firebaseuno

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.firebaseuno.dto.FirestoreUsuarioDto
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val CODIGO_INICIO_SESION = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonLogin = findViewById<Button>(R.id.btn_login)
        botonLogin.setOnClickListener {
            llamarLoginUsuario()
        }

        val botonSalir = findViewById<Button>(R.id.btn_salir)
        botonSalir.setOnClickListener {
            solicitarSalirDelAplicativo()
        }

        val botonProducto = findViewById<Button>(R.id.btn_producto)
        botonProducto.setOnClickListener {
            irProducto()
        }

        val botonRestaurante = findViewById<Button>(R.id.btn_restaurante)
        botonRestaurante.setOnClickListener {
            val intent = Intent(this, DRestaurante::class.java)
            startActivity(intent)
        }

        val botonOrden = findViewById<Button>(R.id.btn_orden)
        botonOrden.setOnClickListener {
            val intent = Intent(this, EOrdenes::class.java)
            startActivity(intent)
        }
    }

    fun llamarLoginUsuario(){
        val providers = arrayListOf(
            // Lista de proveedores
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
            CODIGO_INICIO_SESION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODIGO_INICIO_SESION -> {
                if (resultCode == Activity.RESULT_OK) {
                    val usuario: IdpResponse? = IdpResponse.fromResultIntent(data)
                    if (usuario != null) {
                        // Flujo - Nuevo Usuario
                        if (usuario.isNewUser == true) {
                            Log.i("firebase-login", "Nuevo Usuario")
                            registrarUsuarioPorPrimeraVez(usuario)
                        }
                        // Flujo - Usuario ya registrado
                        else {
                            setearUsuarioFirebase()
                            Log.i("firebase-login", "Usuario Antiguo")
                        }
                    }
                } else {
                    Log.i("firebase-login", "El usuario cancelo")
                }
            }
        }
    }

    fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse) {
        val usuarioLogeado = FirebaseAuth
            .getInstance()
            .currentUser
        if (usuario.email != null && usuarioLogeado != null) {
            val db = Firebase.firestore // Obtener referencia
            val rolesUsuario = arrayListOf("usuario") // Arreglo con los permisos
            val nuevoUsuario = hashMapOf<String, Any>(
                "roles" to rolesUsuario,
                "uid" to usuarioLogeado.uid,
                "email" to usuario.email.toString()
            )

            val identificadorUsuario = usuario.email

            db.collection("usuario")
                // 1ra Forma -> Firestore crea el identificador
                //.add(nuevoUsuario)

                // 2da Forma -> Se crea manualmente el identificador
                .document(identificadorUsuario.toString())
                .set(nuevoUsuario)

                .addOnSuccessListener {
                    Log.i("firebase-firestore", "Se creo")
                    setearUsuarioFirebase()
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore", "Fallo")
                }
        }
    }

    fun setearUsuarioFirebase(){
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if(usuarioLocal != null){
            if(usuarioLocal.email != null){
                // Buscar en Firestore el usuario y traerlo con todos los datos
                val db = Firebase.firestore
                val referencia = db
                    .collection("usuario")
                    .document(usuarioLocal.email.toString())

                referencia
                    .get()
                    .addOnSuccessListener {
                        val usuarioCargado = it.toObject(FirestoreUsuarioDto::class.java)
                        if (usuarioCargado != null) {
                            BAuthUsuario.usuario = BUsuarioFirebase(
                                usuarioCargado.uid,
                                usuarioCargado.email,
                                usuarioCargado.roles
                            )
                            setearBienvenida()
                        }
                        Log.i("firebase-firestore", "Usuario cargado")
                    }
                    .addOnFailureListener{
                        Log.i("firebase-firestore", "Fallo cargar usuario")
                    }
            }
        }
    }

    fun setearBienvenida(){
        val textViewBienvenida = findViewById<TextView>(R.id.tv_bienvenida)
        val botonLogin = findViewById<Button>(R.id.btn_login)
        val botonSalir = findViewById<Button>(R.id.btn_salir)
        val botonProducto = findViewById<Button>(R.id.btn_producto)
        val botonRestaurante = findViewById<Button>(R.id.btn_restaurante)

        if(BAuthUsuario.usuario != null){
            botonLogin.visibility = View.INVISIBLE
            botonSalir.visibility = View.VISIBLE
            botonProducto.visibility = View.VISIBLE
            botonRestaurante.visibility = View.VISIBLE
            textViewBienvenida.text = "Bienvenido ${BAuthUsuario.usuario?.email}"
        } else {
            botonLogin.visibility = View.VISIBLE
            botonSalir.visibility = View.INVISIBLE
            botonProducto.visibility = View.INVISIBLE
            botonRestaurante.visibility = View.INVISIBLE
            textViewBienvenida.text = "Ingresa al aplicativo"
        }
    }

    fun solicitarSalirDelAplicativo() {
        AuthUI
            .getInstance()
            .signOut(this)
            .addOnCompleteListener {
                BAuthUsuario.usuario = null
                setearBienvenida()
            }
    }

    fun irProducto() {
        val intent = Intent(
            this,
            CProducto::class.java
        )
        startActivity(intent)
    }

}