package com.github.nabin0.audioplayer.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * A Data class to represent audio object for demo will be changed later
 */
data class Audio(
    val id: Long?,
    val audioUri: Uri?,
    val displayName: String?,
    val albumArtUrl: String?,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Int?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(audioUri, flags)
        parcel.writeString(displayName)
        parcel.writeString(albumArtUrl)
        parcel.writeString(artist)
        parcel.writeString(title)
        parcel.writeString(data)
        parcel.writeValue(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Audio> {
        override fun createFromParcel(parcel: Parcel): Audio {
            return Audio(parcel)
        }

        override fun newArray(size: Int): Array<Audio?> {
            return arrayOfNulls(size)
        }
    }
}