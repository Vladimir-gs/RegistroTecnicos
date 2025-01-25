package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
){
    suspend fun save(ticket: TicketsEntity) = ticketDao.save(ticket)
    suspend fun getTicket(ticketId: Int) = ticketDao.find(ticketId)
    suspend fun delete(ticket: TicketsEntity) = ticketDao.delete(ticket)
    fun getAll() = ticketDao.getAll()
}