package edu.ucne.registrotecnicos.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val tecnicoRepository: TecnicoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
        getTecnicos()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterTickets()
    }

    private fun filterTickets() {
        viewModelScope.launch {
            ticketRepository.getAll().collect { tickets ->
                val filteredTickets = if (_uiState.value.searchQuery.isBlank()) {
                    tickets
                } else {
                    tickets.filter { ticket ->
                        ticket.cliente.contains(_uiState.value.searchQuery, ignoreCase = true) ||
                                ticket.asunto.contains(_uiState.value.searchQuery, ignoreCase = true) ||
                                ticket.descripcion.contains(_uiState.value.searchQuery, ignoreCase = true)
                    }
                }
                _uiState.update {
                    it.copy(tickets = filteredTickets)

                }
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            val errorMessage = validate()
            if (errorMessage != null) {
                _uiState.update { it.copy(errorMessage = errorMessage) }
            } else {
                ticketRepository.save(_uiState.value.toEntity())
                nuevo()
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
            ticketRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getAll().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
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
    val tickets: List<TicketsEntity> = emptyList(),
    val tecnicos: List<TecnicosEntity> = emptyList(),
    val searchQuery: String = ""
)

fun UiState.toEntity() = TicketsEntity(
    ticketId = ticketId,
    prioridad = prioridadId,
    fecha = fecha,
    cliente = cliente,
    asunto = asunto,
    descripcion = descripcion,
    tecnicoId = tecnicoId,
)