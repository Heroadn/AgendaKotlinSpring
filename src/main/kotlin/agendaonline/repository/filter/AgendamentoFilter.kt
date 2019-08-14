package agendaonline.repository.filter

import agendaonline.model.StatusAgendamento
import java.time.LocalDate

class AgendamentoFilter{
    var registro: LocalDate? = null
    var agendamento: LocalDate? = null
    var status: StatusAgendamento? = null
}