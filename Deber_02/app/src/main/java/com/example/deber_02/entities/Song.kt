package com.example.deber_02.entities

import android.os.Parcel
import android.os.Parcelable

class Song(
    val nombre: String?,
    val artista: String?,
    val minutos: Float,
    val cover: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "${nombre} by ${artista}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(artista)
        parcel.writeString(cover)
        parcel.writeFloat(minutos)
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}