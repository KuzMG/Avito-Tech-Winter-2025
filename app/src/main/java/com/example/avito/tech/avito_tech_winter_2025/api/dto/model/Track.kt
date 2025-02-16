package com.example.avito.tech.avito_tech_winter_2025.api.dto.model

import android.os.Parcel
import android.os.Parcelable

data class Track(
    val title: String,
    val preview: String,
    val artist: Artist,
    val album: Album
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Artist::class.java.classLoader)!!,
        parcel.readParcelable(Album::class.java.classLoader)!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(preview)
        parcel.writeParcelable(artist, 0)
        parcel.writeParcelable(album, 0)
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
