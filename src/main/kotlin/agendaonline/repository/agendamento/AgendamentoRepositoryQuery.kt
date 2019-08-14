package agendaonline.repository.agendamento

import agendaonline.model.Agendamento
import agendaonline.repository.filter.AgendamentoFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AgendamentoRepositoryQuery{
    fun filtrar(agendamentoFilter: AgendamentoFilter, pageable: Pageable): Page<Agendamento>
}