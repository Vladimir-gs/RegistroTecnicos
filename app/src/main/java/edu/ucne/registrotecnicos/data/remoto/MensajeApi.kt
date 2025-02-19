package edu.ucne.registrotecnicos.data.remoto

import edu.ucne.registrotecnicos.data.remoto.dto.MensajeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MensajeApi {
    @GET("api/mensajes/{id}")
    suspend fun getMensaje(@Path("id") id: Int): MensajeDto

    @GET("api/mensajes")
    suspend fun getAllMensajes(): List<MensajeDto>

    @POST("api/mensajes")
    suspend fun saveMensaje(@Body mensaje: MensajeDto)

    @DELETE("api/mensajes")
    suspend fun deleteMensaje(@Path("id") id: Int): Response<Void?>
}