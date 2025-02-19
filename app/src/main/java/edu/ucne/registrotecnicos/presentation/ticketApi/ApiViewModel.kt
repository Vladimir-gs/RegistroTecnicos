package edu.ucne.registrotecnicos.presentation.ticketApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remoto.Resource
import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import edu.ucne.registrotecnicos.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketApiViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
    }

    fun save() {
        viewModelScope.launch {
            val errorMessage = validate()
            if (errorMessage != null) {
                _uiState.update { it.copy(errorMessage = errorMessage) }
            } else {
                apiRepository.saveTicket(_uiState.value.toEntity())
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
        _uiState.value = UiState()
    }

    fun delete() {
        viewModelScope.launch {
            apiRepository.deleteTicket(_uiState.value.ticketId!!)
        }
    }

    fun selectedTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticketApi = apiRepository.getTicket(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticketApi?.ticketId,
                        fecha = ticketApi?.fecha,
                        prioridadId = ticketApi?.prioridad ?: 0,
                        cliente = ticketApi?.cliente ?: "",
                        asunto = ticketApi?.asunto ?: "",
                        descripcion = ticketApi?.descripcion ?: "",
                        tecnicoId = ticketApi?.tecnicoId ?: 0
                    )
                }
            }
        }
    }

    fun getTickets() {
        viewModelScope.launch {
            apiRepository.getAllTickets().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isloading = true)
                        }
                    }

                    is Resource.Suceess -> {
                        _uiState.update {
                            it.copy(
                                tickets = result.data ?: emptyList(),
                                isloading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isloading = false
                            )
                        }
                    }
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
    val isloading: Boolean = false,
    val tecnicoId: Int = 0,
    val errorMessage: String? = null,
    val tickets: List<TicketDto> = emptyList(),
)

fun UiState.toEntity() = TicketDto(
    ticketId = ticketId,
    prioridad = prioridadId,
    fecha = fecha,
    cliente = cliente,
    asunto = asunto,
    descripcion = descripcion,
    tecnicoId = tecnicoId
)