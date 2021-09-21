package com.example.examen02.entities

import android.os.Parcel
import android.os.Parcelable

class Departamento (
    var id: String?,
    var nombre: String?,
    var numeroHabitaciones: Int,
    var numeroBanos: Int,
    var areaM2: Float,
    var valor: Float
) : Parcelable {

    var edificio: Edificio? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat()
    ) {
        edificio = parcel.readParcelable(Edificio::class.java.classLoader)
    }

    override fun toString(): String {
        return "$nombre\n" +
                "   |-> Habitaciones: $numeroHabitaciones\n" +
                "   |-> Banos: $numeroBanos\n" +
                "   |-> Area: $areaM2 m2\n" +
                "   '-> Valor: $$valor"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeInt(numeroHabitaciones)
        parcel.writeInt(numeroBanos)
        parcel.writeFloat(areaM2)
        parcel.writeFloat(valor)
        parcel.writeParcelable(edificio, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Departamento> {
        override fun createFromParcel(parcel: Parcel): Departamento {
            return Departamento(parcel)
        }

        override fun newArray(size: Int): Array<Departamento?> {
            return arrayOfNulls(size)
        }
    }
}