package edu.ucne.registrotecnicos.presentation.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import kotlinx.coroutines.launch

class TecnicoViewModel(private val tecnicoDb: TecnicoDb) : ViewModel() {
    fun deleteTecnico(tecnico: TecnicosEntity) {
        viewModelScope.launch {
            tecnicoDb.tecnicoDao().delete(tecnico)
        }
    }

    fun saveTecnico(tecnico: TecnicosEntity) {
        viewModelScope.launch {
            tecnicoDb.tecnicoDao().save(tecnico)
        }
    }
}
