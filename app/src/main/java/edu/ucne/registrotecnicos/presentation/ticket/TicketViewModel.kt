package edu.ucne.registrotecnicos.presentation.ticket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity
import edu.ucne.registrotecnicos.data.remoto.Resource
import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import edu.ucne.registrotecnicos.data.repository.ApiRepository
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val tecnicoRepository: TecnicoRepository,
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
        getTecnicos()
    }

    fun save() {
        viewModelScope.launch {
            val errorMessage = validate()
            if (errorMessage != null) {
                _uiState.update { it.copy(errorMessage = errorMessage) }
            } else {
                val ticket = _uiState.value.toEntity()
                // Guardamos primero en local
                ticketRepository.save(ticket)

                // Intentamos guardar en la API
                try {
                    apiRepository.saveTicket(ticket.toTicketDto())
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessage = "Ticket guardado localmente. Sincronización pendiente: ${e.message}")
                    }
                }

                nuevo()
            }
        }
    }

    fun edit () {
        viewModelScope.launch {
            val errorMessage = validate()
            if (errorMessage != null) {
                _uiState.update { it.copy(errorMessage = errorMessage) }
            } else {
                val ticket = _uiState.value.toEntity()
                // Guardamos primero en local
                ticketRepository.save(ticket)

                try{
                    apiRepository.updateTicket(ticket.ticketId ?: 0, ticket.toTicketDto())
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(errorMessage = "Ticket editado localmente. Sincronización pendiente: ${e.message}")
                    }
                }
            }
        }
    }

    private fun validate(): String? {
        return when {
            _uiState.value.cliente.isBlank() -> "El nombre del cliente no puede estar vacío"
            _uiState.value.asunto.isBlank() -> "El asunto no puede estar vacío"
            _uiState.value.descripcion.isBlank() -> "La descripción no puede estar vacía"
            _uiState.value.tecnicoId == 0 -> "El técnico no puede estar vacío"
            _uiState.value.prioridadId == 0 -> "La prioridad no puede estar vacía"
            _uiState.value.fecha == null -> "La fecha no puede estar vacía"
            else -> null
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                ticketId = null,
                fecha = Date(),
                cliente = "",
                asunto = "",
                descripcion = "",
                prioridadId = 0,
                tecnicoId = 0,
                errorMessage = null
            )
        }
    }

    fun selectedTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.getTicket(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        fecha = ticket?.fecha,
                        prioridadId = ticket?.prioridad ?: 0,
                        cliente = ticket?.cliente ?: "",
                        asunto = ticket?.asunto ?: "",
                        descripcion = ticket?.descripcion ?: "",
                        tecnicoId = ticket?.tecnicoId ?: 0
                    )
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            val ticket = _uiState.value.toEntity()
            val ticketId = ticket.ticketId ?: return@launch

            try {
                // Aqui eliino local
                ticketRepository.delete(ticket)

                // aqui en la API
                try {
                    Log.d("TicketViewModel", "Intentando eliminar ticket $ticketId de la API")
                    apiRepository.deleteTicket(ticketId)
                    Log.d("TicketViewModel", "Ticket $ticketId eliminado de la API con éxito")
                } catch (e: HttpException) {
                    val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
                    Log.e("TicketViewModel", "HttpException al eliminar de API: $errorMessage")
                    _uiState.update {
                        it.copy(errorMessage = "Ticket eliminado localmente, error al eliminar de API: $errorMessage")
                    }
                } catch (e: Exception) {
                    Log.e("TicketViewModel", "Error al eliminar de API: ${e.message}")
                    _uiState.update {
                        it.copy(errorMessage = "Ticket eliminado localmente, error al eliminar de API: ${e.message}")
                    }
                }

            } catch (e: Exception) {
                Log.e("TicketViewModel", "Error al eliminar localmente: ${e.message}")
                _uiState.update {
                    it.copy(errorMessage = "Error al eliminar el ticket: ${e.message}")
                }
            }
        }
    }


    private fun getTickets() {
        viewModelScope.launch {
            // Primero obtenemos los tickets locales para mostrarlos inmediatamente
            launch {
                ticketRepository.getAll().collect { localTickets ->
                    _uiState.update {
                        it.copy(tickets = localTickets)
                    }
                }
            }

            // Luego sincronizamos con la API
            launch {
                apiRepository.getAllTickets().collect { result ->
                    when (result) {
                        is Resource.Suceess -> { // Nota: corrige esto a Resource.Success
                            // Mantenemos un mapa de IDs para verificar después
                            val existingTickets = mutableMapOf<Int, TicketsEntity>()

                            // Obtenemos todos los tickets locales una sola vez para comparar
                            _uiState.value.tickets.forEach { ticket ->
                                ticket.ticketId?.let { id ->
                                    existingTickets[id] = ticket
                                }
                            }

                            result.data?.forEach { ticketDto ->
                                // Usamos el Upsert que ya tienes implementado
                                // Pero evitamos duplicados verificando la existencia
                                if (ticketDto.ticketId !in existingTickets.keys ||
                                    hasChanges(existingTickets[ticketDto.ticketId], ticketDto)) {
                                    ticketRepository.save(
                                        TicketsEntity(
                                            ticketId = ticketDto.ticketId,
                                            fecha = ticketDto.fecha,
                                            cliente = ticketDto.cliente,
                                            asunto = ticketDto.asunto,
                                            descripcion = ticketDto.descripcion,
                                            prioridad = ticketDto.prioridad,
                                            tecnicoId = ticketDto.tecnicoId
                                        )
                                    )
                                }
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(errorMessage = result.message) }
                        }
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
        }
    }

    // Función auxiliar para verificar si hay cambios entre el ticket local y el de la API
    private fun hasChanges(localTicket: TicketsEntity?, apiTicket: TicketDto): Boolean {
        if (localTicket == null) return true

        return localTicket.cliente != apiTicket.cliente ||
                localTicket.asunto != apiTicket.asunto ||
                localTicket.descripcion != apiTicket.descripcion ||
                localTicket.prioridad != apiTicket.prioridad ||
                localTicket.tecnicoId != apiTicket.tecnicoId
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente, errorMessage = null)
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto, errorMessage = null)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion, errorMessage = null)
        }
    }

        fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha, errorMessage = null)
        }
    }

    fun onPrioridadChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId, errorMessage = null)
        }
    }

    fun onTecnicoChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId, errorMessage = null)
        }
    }
}

data class UiState(
    val ticketId: Int? = null,
    val fecha: Date? = null,
    val prioridadId: Int = 0,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int = 0,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val tickets: List<TicketsEntity> = emptyList(),
    val tecnicos: List<TecnicosEntity> = emptyList(),
)

fun TicketsEntity.toTicketDto() = TicketDto(
    ticketId = ticketId ?: 0,
    fecha = fecha,
    cliente = cliente,
    asunto = asunto,
    descripcion = descripcion,
    prioridad = prioridad,
    tecnicoId = tecnicoId
)

fun UiState.toEntity() = TicketsEntity(
    ticketId = ticketId,
    prioridad = prioridadId,
    fecha = fecha,
    cliente = cliente,
    asunto = asunto,
    descripcion = descripcion,
    tecnicoId = tecnicoId
)