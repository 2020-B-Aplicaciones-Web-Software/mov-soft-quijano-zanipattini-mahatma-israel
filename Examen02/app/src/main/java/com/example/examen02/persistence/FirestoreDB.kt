package com.example.examen02.persistence

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat


object FirestoreDB {

    // Firestore
    val db = Firebase.firestore
    val coleccionEdificio = db.collection("Edificio")
    // Fecha format
    val sdf = SimpleDateFormat("dd/MM/yyyy")

    fun subcoleccionDepartamento(edificioDoc: String): CollectionReference {
        return db.collection("Edificio/${edificioDoc}/Departamento")
    }
}