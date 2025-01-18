package edu.ucne.registrotecnicos.di

import edu.ucne.registrotecnicos.data.local.database.TecnicoDb

import android.content.Context
import androidx.room.Room

object TecnicoDbProvider {
    private var db: TecnicoDb? = null

    fun getInstance(context: Context): TecnicoDb {
        return db ?: synchronized(this) {
            db ?: buildDatabase(context).also { db = it }
        }
    }

    private fun buildDatabase(context: Context): TecnicoDb {
        return Room.databaseBuilder(
            context.applicationContext,
            TecnicoDb::class.java,
            "Ticket.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}