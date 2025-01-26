package edu.ucne.registrotecnicos.presentation.mensaje

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.repository.MensajeRepository
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MensajeViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository,
    private val tecnicoRepository: TecnicoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMensajes()
        getTecnicos()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save() {
        viewModelScope.launch {
            val errorMessage = validate()
            if (errorMessage != null) {
                _uiState.update { it.copy(errorMessage = errorMessage) }
            } else {

                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                val formattedDateTime = currentDateTime.format(formatter)

                _uiState.update { it.copy(fecha = formattedDateTime) }

                val nuevoMensaje = _uiState.value.toEntity()
                mensajeRepository.save(nuevoMensaje)

                _uiState.update {
                    it.copy(
                        mensajes = it.mensajes + nuevoMensaje,
                        mensaje = "",
                        tecnicoId = 0
                    )
                }
            }
        }
    }

    private fun validate(): String? {
        return when {
            _uiState.value.mensaje.isBlank() -> "El mensaje no puede estar vacío"
            _uiState.value.tecnicoId == 0 -> "El técnico no puede estar vacío"
            else -> null
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                mensajeId = null,
                mensaje = "",
                fecha = "",
                tecnicoId = 0,
                errorMessage = null,
            )
        }
    }

    fun selectedMensaje(mensajeId: Int) {
        viewModelScope.launch {
            if (mensajeId > 0) {
                val mensaje = mensajeRepository.getMensaje(mensajeId)
                _uiState.update {
                    it.copy(
                        mensajeId = mensaje?.mensajeId,
                        mensaje = mensaje?.mensaje ?: "",
                        fecha = mensaje?.fecha ?: "",
                        tecnicoId = mensaje?.tecnicoId ?: 0,
                    )
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            mensajeRepository.delete(_uiState.value.toEntity())
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

    private fun getMensajes() {
        viewModelScope.launch {
            mensajeRepository.getAll().collect { mensajes ->
                _uiState.update {
                    it.copy(mensajes = mensajes)
                }
            }
        }
    }

    fun onMensajeChange(mensaje: String) {
        _uiState.update {
            it.copy(mensaje = mensaje, errorMessage = null)
        }
    }

    fun onFechaChange(fecha: String) {
        _uiState.update {
            it.copy(fecha = fecha, errorMessage = null)
        }
    }

    fun onTecnicoChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId, errorMessage = null)
        }
    }
}

data class UiState(
    val mensajeId: Int? = null,
    val mensaje: String = "",
    val fecha: String = "",
    val tecnicoId: Int = 0,
    val errorMessage: String? = null,
    val mensajes: List<MensajeEntity> = emptyList(),
    val tecnicos: List<TecnicosEntity> = emptyList(),
)

fun UiState.toEntity() = MensajeEntity(
    mensajeId = mensajeId,
    mensaje = mensaje,
    fecha = fecha,
    tecnicoId = tecnicoId,
)
