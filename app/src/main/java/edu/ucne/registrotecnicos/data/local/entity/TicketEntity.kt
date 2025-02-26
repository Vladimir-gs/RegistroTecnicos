package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Tickets")
data class TicketsEntity(
    @PrimaryKey
    val ticketId: Int? = null,
    val fecha: Date? = null,
    val prioridad: Int = 0,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int = 0
)