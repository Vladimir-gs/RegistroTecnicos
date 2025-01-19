package edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object TecnicoList : Screen()
    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

}