package com.ms.smogometr_2.db

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val uid: Int =0,
    val value: String,
    val date: Date,
    val location: Location
)
