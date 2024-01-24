package agh.ms.smogometr_1.data.measurement

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Measurement::class],
    version = 2,
    exportSchema = false
)

abstract class MeasurementDatabase: RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
    companion object {
        @Volatile
        private var Instance: MeasurementDatabase?= null

        fun getDatabase(context: Context): MeasurementDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, MeasurementDatabase::class.java, "measurement_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}