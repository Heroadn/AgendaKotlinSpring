package agendaonline.model

enum class StatusAgendamento(s: String) {
    ABERTO("ABERTO"),
    ATENDIDO("ATENDIDO"),
    CANCELADO("CANCELADO"),
    FECHADO("FECHADO")
}