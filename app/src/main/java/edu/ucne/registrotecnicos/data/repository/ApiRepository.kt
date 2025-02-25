package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.remoto.RemoteDataSource
import edu.ucne.registrotecnicos.data.remoto.Resource
import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val dataSource: RemoteDataSource
){
    fun getAllTickets(): Flow<Resource<List<TicketDto>>> = flow {
        try {
            emit(Resource.Loading())
            val tickets = dataSource.getAllTickets()
            emit(Resource.Suceess(tickets))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("ArticuloRepository", "HttpException: $errorMessage")
            emit(Resource.Error("Error de conexion $errorMessage"))
        }catch (e: Exception){
            Log.e("ArticuloRepository", "Exception: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    suspend fun updateTicket(id: Int, ticketDto: TicketDto) = dataSource.updateTicket(id, ticketDto)

    suspend fun getTicket(id: Int) = dataSource.getTicket(id)

    suspend fun saveTicket(ticketDto: TicketDto) = dataSource.saveTicket(ticketDto)

    suspend fun deleteTicket(id: Int): Resource<Boolean> {
        return try {
            dataSource.deleteTicket(id)
            Resource.Suceess(true)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("ApiRepository", "HttpException: $errorMessage")
            Resource.Error("Error de conexión $errorMessage")
        } catch (e: Exception) {
            Log.e("ApiRepository", "Exception: ${e.message}")
            Resource.Error("Error: ${e.message}")
        }
    }

}