package com.example.pollingstar

import android.os.Parcel
import android.os.Parcelable

data class Options(
    val uid: String? = "",
    val value: String = "",
    var votes: Int = 0
)
/* : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(value)
        parcel.writeInt(votes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Options> {
        override fun createFromParcel(parcel: Parcel): Options {
            return Options(parcel)
        }

        override fun newArray(size: Int): Array<Options?> {
            return arrayOfNulls(size)
        }
    }

}
        */