package agendaonline.repository.agendamento

import agendaonline.model.Agendamento
import agendaonline.model.Agendamento_
import agendaonline.repository.filter.AgendamentoFilter
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

class AgendamentoRepositoryImpl(
    var manager:EntityManager
) : AgendamentoRepositoryQuery{

    override fun filtrar(agendamentoFilter: AgendamentoFilter, pageable: Pageable): Page<Agendamento> {
        var builder:CriteriaBuilder = manager.criteriaBuilder
        var criteria:CriteriaQuery<Agendamento> = builder.createQuery(Agendamento::class.java)
        var root: Root<Agendamento> = criteria.from(Agendamento::class.java)

        val predicates = criarRestricoes(agendamentoFilter, builder, root)
        criteria.where(*predicates)

        val query = manager.createQuery(criteria)
        adicionarRestricoesDePaginacao(query, pageable)

        return PageImpl(query.resultList, pageable, total(agendamentoFilter))
    }

    private fun total(agendamentoFilter: AgendamentoFilter): Long {
        val builder = manager.criteriaBuilder
        val criteria = builder.createQuery(Long::class.java)
        val root = criteria.from(Agendamento::class.java)

        val predicates = criarRestricoes(agendamentoFilter, builder, root)
        criteria.where(*predicates)

        criteria.select(builder.count(root))
        return manager.createQuery(criteria).singleResult
    }

    private fun adicionarRestricoesDePaginacao(query: TypedQuery<*>, pageable: Pageable) {
        val paginaAtual = pageable.pageNumber
        val totalDeResguistrosPorPagina = pageable.pageSize
        val primeiroRegistroDa = paginaAtual * totalDeResguistrosPorPagina

        query.firstResult = primeiroRegistroDa
        query.maxResults = totalDeResguistrosPorPagina
    }

    private fun criarRestricoes(agendamentoFilter: AgendamentoFilter, builder: CriteriaBuilder,
                                root: Root<Agendamento>): Array<Predicate> {
        val predicates = ArrayList<Predicate>()

        if (!StringUtils.isEmpty(agendamentoFilter.registro)) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get(Agendamento_.data_registro), agendamentoFilter.registro))
        }

        if (!StringUtils.isEmpty(agendamentoFilter.agendamento)) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get(Agendamento_.data_agendamento), agendamentoFilter.agendamento))
        }
        return predicates.toTypedArray()
    }

}