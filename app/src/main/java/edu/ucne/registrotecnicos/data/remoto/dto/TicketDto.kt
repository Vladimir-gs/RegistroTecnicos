package edu.ucne.registrotecnicos.data.remoto.dto

import java.util.Date

data class TicketDto(
    val ticketId: Int? = null,
    val cliente: String = "",
    val fecha: Date? = null,
    val asunto: String = "",
    val descripcion: String = "",
    val prioridad: Int = 0,
    val tecnicoId: Int = 0
)