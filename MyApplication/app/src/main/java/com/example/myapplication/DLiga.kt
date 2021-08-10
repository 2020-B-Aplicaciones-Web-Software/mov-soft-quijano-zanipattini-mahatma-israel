package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable

class DLiga (
    val nombre: String?,
    val descripcion: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DLiga> {
        override fun createFromParcel(parcel: Parcel): DLiga {
            return DLiga(parcel)
        }

        override fun newArray(size: Int): Array<DLiga?> {
            return arrayOfNulls(size)
        }
    }

}