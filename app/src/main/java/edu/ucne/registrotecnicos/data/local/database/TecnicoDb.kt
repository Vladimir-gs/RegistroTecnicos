package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicosDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity

@Database(
    entities = [TecnicosEntity::class, TicketsEntity::class, MensajeEntity::class],
    version = 4,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicosDao
    abstract fun ticketDao(): TicketDao
    abstract fun mensajeDao(): MensajeDao

    companion object
}