package com.ms.smogometr_2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Measurement::class], version = 1)
abstract class MeasurementDatabase : RoomDatabase(){
    abstract fun MeasurementsDAO() : MeasurementsDAO
}

object MeasurementDb{
    private var db: MeasurementDatabase? = null
    fun getInstance(context: Context):MeasurementDatabase{
        if(db == null){
            db= Room.databaseBuilder(
                context,
                MeasurementDatabase::class.java,
                name = "measurement-database").build()
    }
    return db!!
}
}