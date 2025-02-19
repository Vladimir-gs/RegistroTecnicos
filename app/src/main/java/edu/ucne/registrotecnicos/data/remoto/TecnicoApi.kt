package edu.ucne.registrotecnicos.data.remoto

import edu.ucne.registrotecnicos.data.remoto.dto.TecnicoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TecnicoApi {
    @GET("api/tecnicos/{id}")
    suspend fun getTecnico(@Path("id") id: Int): TecnicoDto

    @GET("api/tecnicos")
    suspend fun getAllTecnicos(): List<TecnicoDto>

    @POST("api/tecnicos")
    suspend fun saveTecnico(@Body tecnico: TecnicoDto)

    @DELETE("api/tecnicos")
    suspend fun deleteTecnico(@Path("id") id: Int): Response<Void?>
}