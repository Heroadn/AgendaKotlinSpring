package agendaonline.repository

import agendaonline.model.Cliente
import agendaonline.repository.cliente.ClienteRepositoryQuery
import org.springframework.data.jpa.repository.JpaRepository

interface ClienteRepository : JpaRepository<Cliente,Long> , ClienteRepositoryQuery{

}