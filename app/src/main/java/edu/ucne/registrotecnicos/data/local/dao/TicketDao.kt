package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao;
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity

@Dao
interface TicketDao {
    @Upsert()
    suspend fun save(tickets: TicketsEntity)

    @Query(
        """
            Select *
            from Tickets
            where ticketId = :id
            limit 1
            """
    )
    suspend fun find(id: Int): TicketsEntity?

    @Delete()
    suspend fun delete(ticketsEntity: TicketsEntity)

    @Query("Select * from Tickets")
    fun getAll(): Flow<List<TicketsEntity>>
}