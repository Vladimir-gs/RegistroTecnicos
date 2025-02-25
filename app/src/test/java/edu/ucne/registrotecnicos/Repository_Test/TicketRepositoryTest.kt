package edu.ucne.registrotecnicos.Repository_Test

import android.util.Log
import app.cash.turbine.test
import edu.ucne.registrotecnicos.data.remoto.RemoteDataSource
import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import edu.ucne.registrotecnicos.data.repository.ApiRepository
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth
import edu.ucne.registrotecnicos.data.remoto.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class TicketRepositoryTest {
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: ApiRepository

    @Before
    fun setup() {
        remoteDataSource = mockk<RemoteDataSource>()
        repository = ApiRepository(remoteDataSource)
    }

    @Test
    fun `getAllTickets should emit Loading then Success with tickets`() = runTest {
        // Given
        val tickets = listOf(
            TicketDto(
                ticketId = 1,
                asunto = "Ticket 1",
                descripcion = "Descripción 1",
                prioridad = 1
            ),
            TicketDto(
                ticketId = 2,
                asunto = "Ticket 2",
                descripcion = "Descripción 2",
                prioridad = 2
            )
        )

        coEvery { remoteDataSource.getAllTickets() } returns tickets

        // When
        repository.getAllTickets().test {
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()

            // Then
            val successResource = awaitItem()
            Truth.assertThat(successResource is Resource.Suceess).isTrue()
            Truth.assertThat(successResource.data).isEqualTo(tickets)

            awaitComplete()
        }
    }

    @Test
    fun `getAllTickets should emit Loading then Error when HttpException occurs`() = runTest {
        // Given
        val errorMessage = "Error 404"
        val httpException = HttpException(Response.error<Any>(404, errorMessage.toResponseBody()))

        coEvery { remoteDataSource.getAllTickets() } throws httpException

        // When
        repository.getAllTickets().test {
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()

            val errorResource = awaitItem()

            Log.d("TEST", "Mensaje recibido: ${errorResource.message}")

            //Then
            Truth.assertThat(errorResource is Resource.Error).isTrue()
            Truth.assertThat(errorResource.message).contains("Error de conexion")

            awaitComplete()
        }
    }


    @Test
    fun `getAllTickets should emit Loading then Error when generic Exception occurs`() = runTest {
        // Given
        val exception = IOException("Network error")

        coEvery { remoteDataSource.getAllTickets() } throws exception

        // When
        repository.getAllTickets().test {
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()

            // Then
            val errorResource = awaitItem()
            Truth.assertThat(errorResource is Resource.Error).isTrue()
            Truth.assertThat(errorResource.message).contains("Error: Network error")

            awaitComplete()
        }
    }

    @Test
    fun `updateTicket should call dataSource updateTicket`() = runTest {
        // Given
        val id = 1
        val ticket = TicketDto(
            ticketId = 1,
            asunto = "Ticket actualizado",
            descripcion = "Nueva descripción",
            prioridad = 2
        )

        coEvery { remoteDataSource.updateTicket(id, ticket) } returns ticket

        // When
        repository.updateTicket(id, ticket)

        // Then
        coVerify { remoteDataSource.updateTicket(id, ticket) }
    }

    @Test
    fun `getTicket should call dataSource getTicket and return the ticket`() = runTest {
        // Given
        val id = 1
        val ticket = TicketDto(
            ticketId = 1,
            asunto = "Ticket 1",
            descripcion = "Descripción 1",
            prioridad = 1
        )

        coEvery { remoteDataSource.getTicket(id) } returns ticket

        // When
        val result = repository.getTicket(id)

        // Then
        Truth.assertThat(result).isEqualTo(ticket)
        coVerify { remoteDataSource.getTicket(id) }
    }

    @Test
    fun `saveTicket should call dataSource saveTicket`() = runTest {
        // Given
        val ticket = TicketDto(
            ticketId = 1,
            asunto = "Ticket 1",
            descripcion = "Descripción 1",
            prioridad = 1
        )

        coEvery { remoteDataSource.saveTicket(ticket) } returns Unit

        // When
        repository.saveTicket(ticket)

        // Then
        coVerify { remoteDataSource.saveTicket(ticket) }
    }

    @Test
    fun `deleteTicket should return Success when deletion is successful`() = runTest {
        // Given
        val id = 1
        coEvery { remoteDataSource.deleteTicket(id) } returns Response.success(null)

        // When
        val result = repository.deleteTicket(id)

        // Then
        Truth.assertThat(result is Resource.Suceess).isTrue() // Corregir "Suceess" -> "Success"
        Truth.assertThat(result.data).isEqualTo(true)
        coVerify { remoteDataSource.deleteTicket(id) } // Verificar que se llamó correctamente
    }


    @Test
    fun `deleteTicket should return Error when HttpException occurs`() = runTest {
        // Given
        val id = 1
        val errorMessage = "Error 404"
        val httpException = HttpException(Response.error<Any>(404, errorMessage.toResponseBody()))

        coEvery { remoteDataSource.deleteTicket(id) } throws httpException

        // When
        val result = repository.deleteTicket(id)

        // Then
        Truth.assertThat(result is Resource.Error).isTrue()
        Truth.assertThat(result.message).contains("Error de conexión")
    }

    @Test
    fun `deleteTicket should return Error when generic Exception occurs`() = runTest {
        // Given
        val id = 1
        val exception = IOException("Network error")

        coEvery { remoteDataSource.deleteTicket(id) } throws exception

        // When
        val result = repository.deleteTicket(id)

        // Then
        Truth.assertThat(result is Resource.Error).isTrue()
        Truth.assertThat(result.message).contains("Error: Network error")
    }
}