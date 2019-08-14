package agendaonline.repository.cliente

import agendaonline.model.Cliente
import agendaonline.repository.filter.ClienteFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ClienteRepositoryQuery{
    fun filtrar(clienteFilter: ClienteFilter, pageable: Pageable): Page<Cliente>
}