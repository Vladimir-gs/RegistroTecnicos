package edu.ucne.registrotecnicos.presentatio.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object TecnicList : Screen()
    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

}