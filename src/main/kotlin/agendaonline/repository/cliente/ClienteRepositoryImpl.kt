package agendaonline.repository.cliente

import agendaonline.model.Cliente
import agendaonline.model.Cliente_
import agendaonline.repository.filter.ClienteFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import java.util.ArrayList
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ClienteRepositoryImpl(
        var manager:EntityManager
)
    : ClienteRepositoryQuery {
    override fun filtrar(clienteFilter: ClienteFilter, pageable: Pageable): Page<Cliente> {
        var builder:CriteriaBuilder = manager.criteriaBuilder
        var criteria:CriteriaQuery<Cliente> = builder.createQuery(Cliente::class.java)
        var root:Root<Cliente> = criteria.from(Cliente::class.java)

        val predicates = criarRestricoes(clienteFilter, builder, root)
        criteria.where(*predicates)

        val query = manager.createQuery(criteria)
        adicionarRestricoesDePaginacao(query, pageable)

        return PageImpl(query.resultList, pageable, total(clienteFilter))
    }

    private fun total(clienteFilter: ClienteFilter): Long {
        val builder = manager.criteriaBuilder
        val criteria = builder.createQuery(Long::class.java)
        val root = criteria.from(Cliente::class.java)

        val predicates = criarRestricoes(clienteFilter, builder, root)
        criteria.where(*predicates)

        criteria.select(builder.count(root))
        return manager.createQuery(criteria).singleResult
    }

    private fun adicionarRestricoesDePaginacao(query: TypedQuery<Cliente>, pageable: Pageable) {
        val paginaAtual = pageable.pageNumber
        val totalDeResguistrosPorPagina = pageable.pageSize
        val primeiroRegistroDa = paginaAtual * totalDeResguistrosPorPagina

        query.firstResult = primeiroRegistroDa
        query.maxResults = totalDeResguistrosPorPagina
    }

    private fun criarRestricoes(clienteFilter: ClienteFilter, builder: CriteriaBuilder, root: Root<Cliente>): Array<Predicate> {
        val predicates = ArrayList<Predicate>()

        if (!StringUtils.isEmpty(clienteFilter.nome)) {
            predicates.add(
                    builder.equal(root.get(Cliente_.nome),clienteFilter.nome))
        }

        if (!StringUtils.isEmpty(clienteFilter.email)) {
            predicates.add(
                    builder.equal(root.get(Cliente_.email),clienteFilter.email))
        }

        if (!StringUtils.isEmpty(clienteFilter.celular)) {
            predicates.add(
                    builder.equal(root.get(Cliente_.celular),clienteFilter.celular))
        }

        return predicates.toTypedArray()
    }
}