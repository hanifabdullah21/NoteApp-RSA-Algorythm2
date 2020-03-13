package com.hanifabdullah21.noteapp_rsa_algorythm

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Note")
data class NoteModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "title")
    var title: String? = "",

    @ColumnInfo(name = "message")
    var message: String? = "",

    @ColumnInfo(name = "created_at")
    var createdAt: String? = ""

) : Parcelable