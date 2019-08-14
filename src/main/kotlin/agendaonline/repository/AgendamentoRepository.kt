package agendaonline.repository

import agendaonline.model.Agendamento
import agendaonline.repository.agendamento.AgendamentoRepositoryQuery
import org.springframework.data.jpa.repository.JpaRepository

interface AgendamentoRepository : JpaRepository<Agendamento,Long> ,AgendamentoRepositoryQuery{
}