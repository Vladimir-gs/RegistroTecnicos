package edu.ucne.registrotecnicos.data.remoto

import androidx.room.Delete
import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicketApi {
    @GET("api/tickets")
    suspend fun getAllTickets(): List<TicketDto>

    @GET("api/tickets/{id}")
    suspend fun getTicket(@Path("id") id: Int): TicketDto

    @POST("api/tickets")
    suspend fun saveTicket(@Body ticket: TicketDto)

    @PUT("api/tickets/{id}")
    suspend fun updateTicket(
        @Path("id") id: Int,
        @Body ticket: TicketDto
    ): TicketDto

    @DELETE("api/tickets")
    suspend fun deleteTicket(@Path("id") id: Int): Response<Void?>
}