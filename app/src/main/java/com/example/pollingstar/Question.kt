package com.example.pollingstar

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val uid : String = "",
    val name : String = "",
    val question: String = "",
    val votes: Int = 0,
    val creatoruid: String = "",
    val options: ArrayList<Options>,
    val answeredby: ArrayList<String>,
    val votechoice : ArrayList<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt().toInt(),
        parcel.readString().toString(),
        parcel.readArrayList(Options::class.java.classLoader) as ArrayList<Options>,
        parcel.readArrayList(String.javaClass.classLoader) as ArrayList<String>,
        parcel.readArrayList(String.javaClass.classLoader) as ArrayList<String>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(question)
        parcel.writeInt(votes)
        parcel.writeString(creatoruid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}

