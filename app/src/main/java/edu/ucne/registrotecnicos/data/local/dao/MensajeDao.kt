package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {
    @Upsert
    suspend fun save(mensaje: MensajeEntity)

    @Query(
        """
            Select *
            from mensajes
            where mensajeId = :id
            limit 1
            """
    )
    suspend fun find(id: Int): MensajeEntity?

    @Delete()
    suspend fun delete(mensajeEntity: MensajeEntity)

    @Query("Select * from mensajes")
    fun getAll(): Flow<List<MensajeEntity>>
}