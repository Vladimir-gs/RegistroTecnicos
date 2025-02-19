package edu.ucne.registrotecnicos.data.remoto

import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
private val ticketApi: TicketApi){

    suspend fun getTicket(id: Int) = ticketApi.getTicket(id)

    suspend fun getAllTickets() = ticketApi.getAllTickets()

    suspend fun saveTicket(ticket: TicketDto) = ticketApi.saveTicket(ticket)

    suspend fun updateTicket(id: Int, ticket: TicketDto) = ticketApi.updateTicket(id, ticket)

    suspend fun deleteTicket(id: Int) = ticketApi.deleteTicket(id)
}