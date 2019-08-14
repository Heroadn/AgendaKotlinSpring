package agendaonline.service

import agendaonline.model.Agendamento
import agendaonline.model.StatusAgendamento
import agendaonline.repository.AgendamentoRepository
import org.springframework.beans.BeanUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class AgendamentoService(var agendamentoRepository: AgendamentoRepository) {

    fun buscarAgendamentoPorId(id: Long): Agendamento {
        var agendamento:Agendamento? = agendamentoRepository.findOne(id)

        if(agendamento == null){
            throw EmptyResultDataAccessException(1)
        }

        return agendamento
    }

    fun salvarAgendamento(agendamento: Agendamento): Agendamento {
        return agendamentoRepository.save(agendamento)
    }

    fun agendamentoAtualizar(agendamento: Agendamento, id: Long): Agendamento {
        var agendamentoBanco:Agendamento = buscarAgendamentoPorId(id)
        BeanUtils.copyProperties(agendamento,agendamentoBanco,"id")
        return agendamentoRepository.save(agendamentoBanco)
    }

    fun agendamentoAtualizarPropriedade(status: StatusAgendamento, id: Long): Agendamento {
        var agendamentoBanco: Agendamento = buscarAgendamentoPorId(id)
        agendamentoBanco.status = status
        return agendamentoRepository.save(agendamentoBanco)
    }
}