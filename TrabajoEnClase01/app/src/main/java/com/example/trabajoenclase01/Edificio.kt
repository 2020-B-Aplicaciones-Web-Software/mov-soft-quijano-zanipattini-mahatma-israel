package com.example.trabajoenclase01

import android.os.Parcel
import android.os.Parcelable

class Edificio (
    var id: Int,
    var nombre: String?,
    var numeroPisos: Int
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    fun getRegisterString(): String {
        return "$id,$nombre,$numeroPisos"
    }

    override fun toString(): String {
        return "$nombre\t\t\t$numeroPisos pisos"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeInt(numeroPisos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Edificio> {
        override fun createFromParcel(parcel: Parcel): Edificio {
            return Edificio(parcel)
        }

        override fun newArray(size: Int): Array<Edificio?> {
            return arrayOfNulls(size)
        }
    }
}