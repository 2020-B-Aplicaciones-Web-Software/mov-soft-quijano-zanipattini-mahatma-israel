package com.example.examen01.entities

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

class Edificio (
    var id: Int,
    var nombre: String?,
    var numeroPisos: Int,
    var areaM2: Float,
    var fechaApertura: Date?,
    var direccion: String?
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readValue(Date::class.java.classLoader) as? Date,
        parcel.readString()
    ) {}

    override fun toString(): String {
        val fecha = sdf.format(fechaApertura)
        return "$nombre\n" +
                "  |-> Pisos: $numeroPisos\n" +
                "  |-> Area: $areaM2 m2\n" +
                "  |-> Apertura: $fecha\n" +
                "  '-> Direccion: $direccion"
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeInt(numeroPisos)
        parcel.writeFloat(areaM2)
        parcel.writeValue(fechaApertura)
        parcel.writeString(direccion)
    }

    companion object CREATOR : Parcelable.Creator<Edificio> {
        val sdf = SimpleDateFormat("dd-MM-yyyy")

        override fun createFromParcel(parcel: Parcel): Edificio {
            return Edificio(parcel)
        }

        override fun newArray(size: Int): Array<Edificio?> {
            return arrayOfNulls(size)
        }
    }
}