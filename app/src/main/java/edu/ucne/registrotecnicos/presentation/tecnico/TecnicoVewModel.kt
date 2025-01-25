package edu.ucne.registrotecnicos.presentation.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicoViewModel @Inject constructor(
    private val repository: TecnicoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(Uistate())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnicos()
    }

    fun save(){
        if (uiState.value.nombre.isBlank() || uiState.value.sueldo <= 0) {
           _uiState.update {
               it.copy(errorMessage = "Verifique los datos")
           }
        }else{
            viewModelScope.launch {
                repository.save(uiState.value.toEntity())
            }
            nuevo()
        }
    }

    fun selectedTecnico(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                val tecnico = repository.getTecnico(tecnicoId)
                _uiState.update {
                    it.copy(
                        tecnicosId = tecnico?.tecnicosId,
                        nombre = tecnico?.nombre ?: "",
                        sueldo = tecnico?.sueldo ?: 0.0
                    )
                }

            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.delete(_uiState.value.toEntity())
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            repository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                nombre = name,
                errorMessage = if (name.isBlank()) "No se Puede Guardar con nombre Vacío"
                else null
            )
        }
    }

    fun onSalaryChange(salary: Double) {
        _uiState.update {
            it.copy(
                sueldo = salary,
                errorMessage = if (salary < 0.0) "No se Puede Guardar con sueldo Negativo"
                else null
            )
        }
    }

    fun nuevo() {
        _uiState.value = Uistate()
    }
}

fun Uistate.toEntity() = TecnicosEntity(
    tecnicosId = this.tecnicosId,
    nombre = this.nombre,
    sueldo = this.sueldo
)

data class Uistate(
    val tecnicosId: Int? = null,
    val nombre: String = "",
    val sueldo: Double = 0.0,
    val errorMessage: String? = null,
    val tecnicos: List<TecnicosEntity> = emptyList(),
)
