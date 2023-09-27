package com.ms.smogometr.db

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurementsTable")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val value: Double,
    val measurementTime: Long,
    val measurementLocation: Location
    )
