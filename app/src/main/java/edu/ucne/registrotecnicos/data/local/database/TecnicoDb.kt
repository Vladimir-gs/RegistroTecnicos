package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.TecnicosDao
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity

@Database(
    entities = [TecnicosEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicosDao
}