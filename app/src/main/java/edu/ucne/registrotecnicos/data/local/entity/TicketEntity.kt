package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

//Ticket(TicketId, Fecha , PrioridadId, Cliente, Asunto, Descripcion, TecnicoId)

@Entity(tableName = "Tickets")
data class TicketsEntity(
    @PrimaryKey
    val ticketId: Int? = null,
    val fecha: LocalDate,
    val prioridadId: Int = 0,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int = 0
)


