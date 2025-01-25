package edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object HomeScreen: Screen()

    @Serializable
    data object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val TecnicoId: Int): Screen()

    @Serializable
    data class TecnicoDelete(val TecnicoId: Int) : Screen()

    @Serializable
    data class TecnicoEdit(val TecnicoId: Int) : Screen()

    @Serializable
    data object TicketList: Screen()

    @Serializable
    data class Ticket(val TicketId: Int): Screen()

    @Serializable
    data class TicketDelete(val TicketId: Int) : Screen()

    @Serializable
    data class TicketEdit(val TicketId: Int) : Screen()


}