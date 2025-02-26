package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao;
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity

@Dao
interface TecnicosDao {
    @Upsert()
    suspend fun save(tecnico: TecnicosEntity)

    @Query(
        """
            Select *
            from Tecnicos
            where tecnicosId = :id
            limit 1
            """
    )
    suspend fun find(id: Int): TecnicosEntity?

    @Delete()
    suspend fun delete(tecnicosEntity: TecnicosEntity)

    @Query("Select * from Tecnicos")
    fun getAll(): Flow<List<TecnicosEntity>>
}